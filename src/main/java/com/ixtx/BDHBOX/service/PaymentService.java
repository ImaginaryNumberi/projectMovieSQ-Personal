package com.ixtx.BDHBOX.service;

import com.ixtx.projectmoviesq.entities.PaymentEntity;
import com.ixtx.projectmoviesq.entities.ReservationEntity;
import com.ixtx.projectmoviesq.entities.UserEntity;
import com.ixtx.projectmoviesq.enums.CommonResult;
import com.ixtx.projectmoviesq.enums.regexp.RegExp;
import com.ixtx.projectmoviesq.mappers.PaymentMapper;
import com.ixtx.projectmoviesq.utils.CryptoUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentService(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    // KHR WORK
    public Map<Object, Object> getSchduleInformationByIndex(HttpSession session) {
        int sessionIndex = (int) session.getAttribute("index");
        return this.paymentMapper.selectScheduleInformationByIndex(sessionIndex);
    }


    // BDH WORK
    public String checkSeatPayment(HttpSession session, String[] headNameArray, String[] seatNameArray) {
        // 스케쥴인덱스
        int sessionIndex = (int) session.getAttribute("index");
        // 스케쥴 상영시간 유효 검사
        // 세션인덱스로 스크린 인덱스 값 가져오기
        int screenIndex = this.paymentMapper.selectScreenIndex(sessionIndex);
        if (screenIndex == 0) {
            //"유효하지 않은 인덱스 값"
            return "invalidIndex";
        }
        // 인원수 길이는 1~8까지인가?
        // 좌석에 아무런 값이 없을 때
        if (headNameArray.length <= 0 || headNameArray.length > 8 || seatNameArray.length == 0) {
            session.removeAttribute("user");
            session.removeAttribute("index");
            // 잘못된 인원수
            return "wrongCount";
        }
        // 인원수 및 좌석수 같은가?
        if (headNameArray.length != seatNameArray.length) {
            // 수가 같지 않음
            return "notEqualCount";
        }
        // 인원수의 일반, 청소년만 들어가있는가?
        for (String type : headNameArray) {
            if (!type.equals("일반") && !type.equals("청소년")) {
                // 잘못된 인원타입
                return "wrongType";
            }
        }
        // 좌석이 존재하는 좌석이름인가?
        for (String seat : seatNameArray) {
            if (this.paymentMapper.selectExitSeat(sessionIndex, seat) == 0) {
                // 존재하지 않는 좌석값
                return "nonexistentSeatName";
            }
        }
        // 스크린 인덱스 세션 저장
        session.setAttribute("screenIndex", screenIndex);
        return "success";
    }


    // BDH && KHR WORK
    @Transactional
    public Map<String, Object> putPaymentReservation(HttpSession session, PaymentEntity payment) {
        // 세션 user 가져오기
        UserEntity user = (UserEntity) session.getAttribute("user");
        Map<String, Object> resultMap = new HashMap<>();
        if (user == null
                || payment == null
                || !RegExp.EMAIL.matches(user.getEmail())
                || !RegExp.CARD_NUMBER.matches(payment.getCardNumber())
                || !RegExp.CARD_PASSWORD.matches(payment.getPaymentPassword())) {
            resultMap.put("result", CommonResult.FAILURE);
            return resultMap;
        }


        // 유효한 카드인지 확인
        String checkNumber = payment.getCardNumber().substring(0, 6);
        if (!isCorrectCard(checkNumber, payment.getPaymentCompany())) {
            resultMap.put("result", CommonResult.FAILURE);
            return resultMap;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String current = sdf.format(new Date());
        // 티켓번호 생성
        String ticketNumber = String.format("%s%s", current, RandomStringUtils.randomNumeric(8));
 
        //BDH WORK
        String[] headNameArray = (String[]) session.getAttribute("headNameArray");
        String[] seatNameArray = (String[]) session.getAttribute("seatNameArray");
     
        //BDH WORK
        // 총 가격 변수 생성
        int paymentAmount = 0;
        for (String headName: headNameArray) {
            if(headName.equals("일반")) {
                paymentAmount += 10000;
            }
            else {
                paymentAmount += 5000;
            }
        }

        // payment setting
        payment.setUserEmail(user.getEmail())
                .setTicketNumber(ticketNumber)
                .setCategory(payment.getCategory().toUpperCase())
                .setPaymentCompany(payment.getPaymentCompany())
                .setBiNumber(checkNumber)
                .setCardNumber(CryptoUtil.hashSha512(payment.getCardNumber()))
                .setPaymentPassword(CryptoUtil.hashSha512(String.format("%s%f%f", payment.getCardNumber(), Math.random(), Math.random())))
                .setPaymentAmount(paymentAmount)
                .setAuthCode("12345678")
                .setPaidAt(new Date())
                .setRefunded(false);

        //BDH WORK
        // 예약 좌석 변수 생성
        String reservedSeatNames = "";
        for (int i = 0; i < seatNameArray.length; i++) {
            if(i == 0) {
                reservedSeatNames += seatNameArray[i];
            }
            else {
                reservedSeatNames += "," + seatNameArray[i];
            }
        }
        //BDH WORK
        // 인원타입 변수 생성
        String typeNames = "";
        for (int i = 0; i < headNameArray.length; i++) {
            if(i == 0) {
                typeNames += headNameArray[i];
            }
            else {
                typeNames += "," + headNameArray[i];
            }
        }


        // BDH WORK
        // 예매 진행
        ReservationEntity reservation = new ReservationEntity();
        reservation.setUserEmail(user.getEmail())
                .setTicketNumber(ticketNumber)
                .setScreenScheduleIndex((int) session.getAttribute("index"))
                .setTicketTotal(((String[]) session.getAttribute("seatNameArray")).length)
                .setReservedSeatName(reservedSeatNames)
                .setExpired(false)
                .setPaymentAmount(paymentAmount)
                .setTypeName(typeNames);


        /*
        //BDH WORK
        // reservation_status 테이블 insert
        // 두 가지 방법
        // Map, parameter 전달 방법
        int sessionIndex = (int) session.getAttribute("index");
        int sessionScreenIndex = (int) session.getAttribute("screenIndex");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sessionIndex", sessionIndex);
        map.put("sessionScreenIndex", sessionScreenIndex);
        for (String seatName : seatNameArray) {
            map.put("seatName", seatName);
            if(this.paymentMapper.insertReservationValueToReservationStatus(map) < 1) {
                throw new RuntimeException();
            }
        }
        */
        //BDH WORK
        // No parameter 및 @RequestParam 전달 방법
        int sessionIndex = (int) session.getAttribute("index");
        int sessionScreenIndex = (int) session.getAttribute("screenIndex");
        for (String seatName : seatNameArray) {
            if(this.paymentMapper.insertReservationValueToReservationStatus(sessionIndex, sessionScreenIndex, seatName) < 1) {
                throw new RuntimeException();
            }
        }

        // BDH WORK && KHR WORK
        // reservations 테이블 insert
        // payment 테이블 insert
        if(this.paymentMapper.insertReservationToReservations(reservation) > 0 && this.paymentMapper.insertPayment(payment) > 0){
            resultMap.put("result",CommonResult.SUCCESS);
            resultMap.put("ticketNumber",ticketNumber);

            // 사용 다한 세션 지우기
            session.removeAttribute("index");
            session.removeAttribute("screenIndex");
            session.removeAttribute("seatNameArray");
            session.removeAttribute("headNameArray");
        }
        else {
            throw new RuntimeException();
        }
        return resultMap;
    }


    // KHR WORK
    public boolean isCorrectCard(String cardNumber, String company) {
        return this.paymentMapper.selectCardBinsByBiNumber(cardNumber, company) > 0;
    }

    public Map<Object, Object> getInformationByTicketNumber(@Param(value = "ticketNumber") String ticketNumber) {
        return this.paymentMapper.selectInformationByTicketNumber(ticketNumber);
    }

}
