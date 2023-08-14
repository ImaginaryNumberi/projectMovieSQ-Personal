package com.ixtx.projectmoviesq.controllers;

import com.ixtx.projectmoviesq.entities.PaymentEntity;
import com.ixtx.projectmoviesq.entities.ReservationEntity;
import com.ixtx.projectmoviesq.entities.UserEntity;
import com.ixtx.projectmoviesq.enums.CommonResult;
import com.ixtx.projectmoviesq.services.PaymentService;
import com.ixtx.projectmoviesq.utils.NCloudUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RequestMapping(value = "payment.do",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getPayment(HttpServletRequest request,
                                   @RequestParam(value = "headNameArray", required = false) String[] headNameArray,
                                   @RequestParam(value = "seatNameArray", required = false) String[] seatNameArray) {


        HttpSession session = request.getSession();
        ModelAndView modelAndView = new ModelAndView("pages/payment");


        // 로그인 되어있는가 확인
        if(session.getAttribute("user") == null) {
            session.removeAttribute("user");
            session.removeAttribute("index");
            // 로그인이 안되어있을시 로그인 창으로 리다이렉트
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        // 세션인덱스 만료 검사
        if (session.getAttribute("index") == null) {
            session.removeAttribute("index");
            modelAndView.setViewName("redirect:/reserve");
            return modelAndView;
        }

        // 받아온 값들 유효성 검사
        String result = this.paymentService.checkSeatPayment(session, headNameArray, seatNameArray);

        if(!result.equals("success")) {
            modelAndView.addObject("result", result);
            return modelAndView;
        }
        // 총가격 계산
        int sum = 0;
        for (String type : headNameArray) {
            if(type.equals("일반")){
                sum += 10000;
            }
            else if (type.equals("청소년")) {
                sum += 5000;
            }
        }
        // 모든 좌석
        String seatOfAll = "";
        for (int i = 0; i < seatNameArray.length; i++) {
            if(i == 0){
                seatOfAll += seatNameArray[i];
            } else {
                seatOfAll += "," + seatNameArray[i];
            }
        }
        // 일반, 청소년 나누기
        int adultCount = 0, kidCount = 0;
        for (String type:headNameArray) {
            if(type.equals("일반")) {
                adultCount += 1;
            }
            else {
                kidCount += 1;
            }
        }

        Map<Object, Object> scheduleInformation = this.paymentService.getSchduleInformationByIndex(session);

        modelAndView.addObject("result", result);
        modelAndView.addObject("movieIndex", scheduleInformation.get("movieIndex"));
        modelAndView.addObject("rating", scheduleInformation.get("rating"));
        modelAndView.addObject("titleKo", scheduleInformation.get("titleKo"));
        modelAndView.addObject("theaterName", scheduleInformation.get("theaterName"));
        modelAndView.addObject("date", scheduleInformation.get("date"));
        modelAndView.addObject("time", scheduleInformation.get("time"));
        modelAndView.addObject("type",headNameArray.length);
        modelAndView.addObject("seatNames",seatOfAll);
        modelAndView.addObject("screenName", scheduleInformation.get("screenName"));
        modelAndView.addObject("adultCount", adultCount);
        modelAndView.addObject("kidCount", kidCount);

        session.setAttribute("headNameArray", headNameArray);
        session.setAttribute("seatNameArray", seatNameArray);


        return modelAndView;

    }

    // 결제 및 예약 진행
    @ResponseBody
    @RequestMapping(value = "pay",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String postPay(HttpServletRequest request,
                          PaymentEntity payment,
                          @SessionAttribute("user")UserEntity user) {
        HttpSession session = request.getSession();
        JSONObject responseObject = new JSONObject();

        // 로그인 되어있는가 확인
        if(session.getAttribute("user") == null) {
            session.removeAttribute("user");
            session.removeAttribute("index");
            session.removeAttribute("screenIndex");
            session.removeAttribute("headNameArray");
            session.removeAttribute("seatNameArray");
            responseObject.put("result", CommonResult.NOT_LOGIN.name().toLowerCase());
            return responseObject.toString();
        }

        // 세션인덱스 만료 검사
        if (session.getAttribute("index") == null) {
            session.removeAttribute("user");
            session.removeAttribute("index");
            session.removeAttribute("screenIndex");
            session.removeAttribute("headNameArray");
            session.removeAttribute("seatNameArray");
            responseObject.put("result", CommonResult.INVALID_INDEX.name().toLowerCase());
            return responseObject.toString();
        }

        // screenIndex 없을 시
        if(session.getAttribute("screenIndex") == null) {
            session.removeAttribute("user");
            session.removeAttribute("index");
            session.removeAttribute("screenIndex");
            session.removeAttribute("headNameArray");
            session.removeAttribute("seatNameArray");
            responseObject.put("result", CommonResult.INVALID_SCREEN_INDEX.name().toLowerCase());
            return responseObject.toString();
        }

        // headNameArray or seatNameArray 세션 없을 시
        if(session.getAttribute("headNameArray") == null || session.getAttribute("seatNameArray") == null) {
            session.removeAttribute("user");
            session.removeAttribute("index");
            session.removeAttribute("screenIndex");
            session.removeAttribute("headNameArray");
            session.removeAttribute("seatNameArray");
            responseObject.put("result", CommonResult.INVALID_ARRAY.name().toLowerCase());
            return responseObject.toString();
        }

        Map<String, Object> finishResult = new HashMap<String, Object>();
        try {
            finishResult = this.paymentService.putPaymentReservation(session, payment);
        } catch (Throwable t) {
            finishResult.put("result", CommonResult.FAILURE);
        }
       
        responseObject.put("result", finishResult.get("result").toString().toLowerCase());

        /*
        System.out.println(finishResult.get("result"));
        System.out.println(finishResult.get("ticketNumber"));
        responseObject.put("result",finishResult.get("result").toString().toLowerCase());
        */

        if (finishResult.get("result").toString().equalsIgnoreCase("success")) {
            String ticketNumber = finishResult.get("ticketNumber").toString();
            Map<Object, Object> reservationResult = this.paymentService.getInformationByTicketNumber(ticketNumber);
            reservationResult.get("typeName");
            responseObject.put("movieIndex", reservationResult.get("movieIndex"));
            responseObject.put("ticketNumber", reservationResult.get("ticketNumber"));
            responseObject.put("movieName", reservationResult.get("movieName"));
            responseObject.put("theater", reservationResult.get("theater"));
            responseObject.put("date", reservationResult.get("date"));
            responseObject.put("screen", reservationResult.get("screen"));
            responseObject.put("time", reservationResult.get("time"));
            responseObject.put("ticketTotal", reservationResult.get("ticketTotal"));

            responseObject.put("adult", countType((String) reservationResult.get("typeName"),"일반"));
            responseObject.put("child",countType((String) reservationResult.get("typeName"),"청소년"));

            responseObject.put("seatName", reservationResult.get("seatName"));
            responseObject.put("cancelTime", reservationResult.get("cancelTime"));
            NCloudUtil.sendSms(user.getContact(), String.format("[Moviesq] 예매완료\n" +
                            "%s\n" +
                            "%s\n" +
                            "%s %s명", ticketNumber,
                    reservationResult.get("movieName"),
                    reservationResult.get("date"), reservationResult.get("ticketTotal")));
        }
        return responseObject.toString();
    }


    public int countType(String string, String type) {
        String[] stringArray = string.split(",");
        int count = 0;
        for (int i = 0; i < stringArray.length; i++) {
            if (type.equals(stringArray[i])) {
                count++;
            }
        }
        return count;
    }
}
