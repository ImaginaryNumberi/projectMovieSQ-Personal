package com.ixtx.BDHBOX.controller;

import com.ixtx.projectmoviesq.dtos.TicketDto;
import com.ixtx.projectmoviesq.entities.ContactCodeEntity;
import com.ixtx.projectmoviesq.entities.PaymentEntity;
import com.ixtx.projectmoviesq.entities.UserEntity;
import com.ixtx.projectmoviesq.enums.CancelTicketResult;
import com.ixtx.projectmoviesq.enums.CommonResult;
import com.ixtx.projectmoviesq.enums.VerifyResult;
import com.ixtx.projectmoviesq.services.MypageService;
import com.ixtx.projectmoviesq.services.UserService;
import com.ixtx.projectmoviesq.utils.CryptoUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping(value = "/mypage")
public class MypageController {

    private final MypageService mypageService;
    private final UserService userService;

    public MypageController(MypageService mypageService, UserService userService) {
        this.mypageService = mypageService;
        this.userService = userService;
    }

    // 예매내역 및 취소내역
    @RequestMapping(value = "ticket", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getTicket(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        ModelAndView modelAndView = new ModelAndView("/mypage/ticketList");
        String mypageURL = "/mypage/ticket";
        if (user == null) {
            session.setAttribute("url", mypageURL);
            return new ModelAndView("redirect:../login");
        }
       TicketDto[] reservedTicket = this.mypageService.getReservedTicket(user.getEmail());
       TicketDto[] canceledTickets = this.mypageService.getCanceledTicket(user.getEmail());
        modelAndView.addObject("total", reservedTicket.length);
        modelAndView.addObject("totalCancel", canceledTickets.length);
        modelAndView.addObject("user", user);
        modelAndView.addObject("reservedTickets", reservedTicket);
        modelAndView.addObject("canceledTickets", canceledTickets);
        return modelAndView;
    }

    // 예매 정보 출력
    @ResponseBody
    @RequestMapping(value = "ticket/information",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getTicketInformation(@RequestParam(value = "index") int index) {
        TicketDto reservation = this.mypageService.getTicketInformation(index);
        JSONObject responseObject = new JSONObject();
        if(reservation == null){
            responseObject.put("result", CommonResult.FAILURE.name().toLowerCase());
        } else {
            responseObject.put("result", CommonResult.SUCCESS.name().toLowerCase());
            responseObject.put("ticketNumber", String.valueOf(reservation.getTicketNumber()));
            responseObject.put("movieName", reservation.getTitleKo());
            responseObject.put("movieDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reservation.getTimeStart()));
            responseObject.put("theater", reservation.getTheaterName());
            responseObject.put("screen", reservation.getScreenName());
            responseObject.put("person", reservation.getTicketTotal());
            responseObject.put("seat", reservation.getReservedSeatName());
        }
        return responseObject.toString();
    }

    // 예매 취소
    @ResponseBody
    @RequestMapping(value = "cancelTicket",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String patchCancelTicket(HttpServletRequest request,
                                    @RequestParam(value = "reservationIndex") int reservationIndex) {

        HttpSession session = request.getSession();
        JSONObject responseObject = new JSONObject();

        // BDH WORK
        // 로그인 되어있는가 확인
        if(session.getAttribute("user") == null) {
            session.removeAttribute("user");
            // 로그인이 안되어있을시 로그인 창으로 리다이렉트
            responseObject.put("result", CancelTicketResult.NOT_LOGIN.name().toLowerCase());
            return responseObject.toString();
        }


        /*
        CancelTicketResult result = this.mypageService.cancelTicket(reservationIndex);
        responseObject.put("result", result.name().toLowerCase());
        */

        // BDH WORK
        CancelTicketResult result;
        try {
            result = this.mypageService.cancelTicket(reservationIndex);
        } catch (Throwable t) {
            result = CancelTicketResult.FAILURE_DB;
        }
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }

    // 관람 내역
    @RequestMapping(value = "/movieLog", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMovieLog(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return new ModelAndView("redirect:../login");
        }
        ModelAndView modelAndView = new ModelAndView("/mypage/movieLog");
        List<TicketDto> reservedTickets = this.mypageService.getMovieLog(user.getEmail());
        modelAndView.addObject("total", reservedTickets.size());
        modelAndView.addObject("user", user);
        modelAndView.addObject("reservedTickets", reservedTickets);
        return modelAndView;
    }

    // 결제 정보 (영수증 출력) 불러오기
    @ResponseBody
    @RequestMapping(value = "/movieLog/payment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMovieLogPayment(@RequestParam(value = "index") String index) {
        PaymentEntity payment = this.mypageService.getPayment(index);
        JSONObject responseObject = new JSONObject();
        if (payment != null) {
            responseObject.put("result", CommonResult.SUCCESS.name().toLowerCase());
            // TODO : DB reset
            responseObject.put("payment", payment.getPaymentCompany());
            responseObject.put("paidAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payment.getPaidAt()));
            responseObject.put("cardNumber", payment.getCardNumber());
            responseObject.put("paymentAmount", payment.getPaymentAmount());
        } else {
            responseObject.put("result", CommonResult.FAILURE.name().toLowerCase());
        }
        return responseObject.toString();
    }

    // 본인 확인
    @RequestMapping(value = "userCheck",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getUserCheck(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return new ModelAndView("redirect:../login");
        }
        ModelAndView modelAndView = new ModelAndView("mypage/userCheck");
        modelAndView.addObject("user", user);
        return modelAndView;
    }


    // 본인 확인 (mypage/userCheck)
    @ResponseBody
    @RequestMapping(value = "userCheck",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String postUserCheck(HttpServletRequest request,
                                UserEntity user) {
        HttpSession session = request.getSession();
        boolean result = this.mypageService.getUserCheck(session, user);
        String code = RandomStringUtils.randomNumeric(6); // 6자리의 임의의 숫자 문자열을 생성하는 기능
        String salt = CryptoUtil.hashSha512(String.format("%s%s%f%f", code, user.getEmail(), Math.random(), Math.random()));
        if (result) {
            session.setAttribute("userCheckResult", salt);
        }
        return String.valueOf(result);
    }

    //     회원 정보 변경
    @RequestMapping(value = "/update", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getUpdate(HttpSession session) {
        UserEntity sessionUser = (UserEntity) session.getAttribute("user");
        String salt = (String) session.getAttribute("userCheckResult");
        if (sessionUser == null) {
            return new ModelAndView("redirect:../");
        }
        if (salt == null) {
            return new ModelAndView("redirect:./userCheck");
        }
        session.removeAttribute(salt);
        UserEntity user  = this.userService.getUserByContact(sessionUser);
        ModelAndView modelAndView = new ModelAndView("mypage/update");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String birth = sdf.format(user.getBirthday());
        modelAndView.addObject("user", user);
        modelAndView.addObject("birth", birth);
        session.removeAttribute("userCheckResult");
        return modelAndView;
    }


    //회원 정보 변경 (mypage/update)
    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public String patchUpdate(UserEntity user,
                              HttpSession session,
                              @RequestParam(value = "uPassword",
                                      required = false) String uPassword,
                              @RequestParam(value = "newContact",
                                      required = false) String newContact,
                              ContactCodeEntity contactCode) {
        VerifyResult result = this.mypageService.updateUserInformation(user, uPassword, newContact, contactCode);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result.name().toLowerCase());
        if (result.name().equalsIgnoreCase("success")) {
            session.setAttribute("user", null);
        }
        return jsonObject.toString();
    }

    //회원 탈퇴
    @ResponseBody
    @RequestMapping(value = "user",
            method = RequestMethod.PATCH)
    public String patchUser(HttpSession session, UserEntity user) {
        boolean result = this.mypageService.updateUserStatus(user);
        if (result) {
            session.setAttribute("user", null);
        }
        return String.valueOf(result);
    }
}