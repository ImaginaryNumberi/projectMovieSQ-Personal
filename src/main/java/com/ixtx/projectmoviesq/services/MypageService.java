package com.ixtx.projectmoviesq.services;

import com.ixtx.projectmoviesq.dtos.TicketDto;
import com.ixtx.projectmoviesq.entities.*;
import com.ixtx.projectmoviesq.enums.CancelTicketResult;
import com.ixtx.projectmoviesq.enums.VerifyResult;
import com.ixtx.projectmoviesq.enums.regexp.RegExp;
import com.ixtx.projectmoviesq.mappers.MypageMapper;
import com.ixtx.projectmoviesq.mappers.UserMapper;
import com.ixtx.projectmoviesq.utils.CryptoUtil;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpSession;
import java.util.*;


@Service
public class MypageService {

    private final MypageMapper mypageMapper;
    private final UserMapper userMapper;

    @Autowired
    public MypageService(MypageMapper mypageMapper, UserMapper userMapper) {
        this.mypageMapper = mypageMapper;
        this.userMapper = userMapper;
    }

    // 정보 확인 : 현재 로그인된 유저와 동일한지
    @Transactional
    public boolean getUserCheck(HttpSession session, UserEntity user) {
        if (session == null
                || user == null) {
            return false;
        }
        UserEntity userSession = (UserEntity) session.getAttribute("user");

        user.setPassword(CryptoUtil.hashSha512(user.getPassword()));

        // 일치하는 회원 정보 가져오기
        UserEntity existingUser = this.mypageMapper.selectUserByEmailPassword(user.getEmail(), user.getPassword());

        if (existingUser == null) {
            return false;
        }

        return userSession.getEmail().equals(existingUser.getEmail()) && userSession.getPassword().equals(existingUser.getPassword());
    }


    // 정보 변경
    @Transactional
    public VerifyResult updateUserInformation(UserEntity user, String uPassword, String newContact, ContactCodeEntity contactCode) {
        if (user == null
                || user.getEmail() == null
                || user.getContact() == null
                || !RegExp.EMAIL.matches(user.getEmail())
                || !RegExp.CONTACT.matches(user.getContact())) {
            return VerifyResult.FAILURE;
        }
        UserEntity existingUser = this.mypageMapper.selectUserByEmail(user.getEmail());
        if (existingUser == null) {
            return VerifyResult.FAILURE;
        }
        if (uPassword != null) {
            if (!RegExp.PASSWORD.matches(uPassword)) {
                return VerifyResult.FAILURE;
            }
            existingUser.setPassword(CryptoUtil.hashSha512(uPassword));
        }
        if (newContact != null) {
            if (!RegExp.CODE.matches(contactCode.getCode())
                    || !RegExp.CONTACT.matches(newContact)
                    || !RegExp.SALT.matches(contactCode.getSalt())) {
                return VerifyResult.FAILURE;
            }
            ContactCodeEntity code = this.userMapper.selectRegisterByContact(newContact);
            if (code.isExpired()) {
                existingUser.setContact(newContact);
            } else {
                return VerifyResult.FAILURE_NOT_VERIFY;
            }
        } else {
            existingUser.setContact(user.getContact());
        }
        return this.mypageMapper.updateUserInformation(existingUser) > 0
                ? VerifyResult.SUCCESS
                : VerifyResult.FAILURE;

    }

    // 회원 탈퇴 (User Status :OKAY -> DELETED 바꾸기)
    @Transactional
    public boolean updateUserStatus(UserEntity user) {
        UserEntity existingUser = this.mypageMapper.selectUserByEmail(user.getEmail());
        if (existingUser == null) {
            return false;
        }
        existingUser.setStatus("DELETED");
        return this.mypageMapper.updateUserStatus(existingUser) > 0;
    }

    // 전체 예약 정보 가져오기
    @Transactional
    public TicketDto[] getReservedTicket(@Param("email") String email) {
        return this.mypageMapper.selectReservationByEmail(email);
    }

    // 해당 예매 정보
    public TicketDto getTicketInformation(@Param("index") int index) {
        return this.mypageMapper.selectReservedInformationByIndex(index);
    }

    // 예약 취소 정보 가져오기
    @Transactional
    public TicketDto[]  getCanceledTicket(@Param("email") String email) {
        return this.mypageMapper.selectCancelReservationByEmail(email);
    }

    // 관람 내역 가져오기
    @Transactional
    public List<TicketDto> getMovieLog(@Param("email") String email) {
        TicketDto[] reservations = this.mypageMapper.selectMovieLogByEmail(email);
        List<TicketDto> reservationList = new ArrayList<>();
        for (TicketDto reservation : reservations) {
            TicketDto ticketDto = new TicketDto();
            ticketDto.setIndex(reservation.getIndex())
                    .setTicketNumber(reservation.getTicketNumber())
                    .setUserEmail(reservation.getUserEmail())
                    .setTicketTotal(reservation.getTicketTotal())
                    .setReservedSeatName(reservation.getReservedSeatName())
                    .setPaymentAmount(reservation.getPaymentAmount())
                    .setTimeStart(reservation.getTimeStart())
                    .setTimeEnd(reservation.getTimeEnd())
                    .setMovieIndex(reservation.getMovieIndex())
                    .setTitleKo(reservation.getTitleKo())
                    .setTheaterName(reservation.getTheaterName())
                    .setScreenName(reservation.getScreenName());
            reservationList.add(ticketDto);
        }
        return reservationList;
    }

    // 결제 정보 가져오기
    public PaymentEntity getPayment(@Param("index") String index) {
        return this.mypageMapper.selectPaymentByTicketNumber(index);
    }

    // 티켓 예매 취소
    @Transactional
    public CancelTicketResult cancelTicket(@Param(value = "reservationIndex") int reservationIndex) {
        ReservationEntity reservation = this.mypageMapper.selectReservationByIndex(reservationIndex);
        if (reservation == null) {
            return CancelTicketResult.FAILURE;
        }
        ScheduleEntity schedule = this.mypageMapper.selectScheduleByIndex(reservation.getScreenScheduleIndex());
        if (schedule == null) {
            return CancelTicketResult.FAILURE;
        }

        // 현재시간이 상영시간 15분전 여부 확인
        Date now = new Date(); // 현재
        Calendar cal = Calendar.getInstance();
        Date startTime = schedule.getTimeStart(); // 상영일
        cal.setTime(startTime);
        cal.add(Calendar.MINUTE, -15);
        Date limitTime = cal.getTime();
        if (limitTime.compareTo(now) < 0) {
            return CancelTicketResult.FAILURE_TIME_EXPIRED;
        }
        reservation.setExpired(true);
        reservation.setCanceledAt(new Date());
        PaymentEntity payment = this.mypageMapper.selectPaymentByTicketNumber(reservation.getTicketNumber());
        if (payment == null) {
            return CancelTicketResult.FAILURE;
        }
        payment.setRefunded(true)
                .setRefundedAt(new Date());

        // BDH WORK
        // 좌석 배열화
        String seatNames = reservation.getReservedSeatName();
        String[] seatNameArray = seatNames.split(",");
        // 스케쥴 인덱스
        int scheduleIndex = reservation.getScreenScheduleIndex();
        // 해당 좌석 지우기
        for (String seatName : seatNameArray) {
            if(this.mypageMapper.deleteSeatNameInReservationStatus(scheduleIndex, seatName) < 1) {
                throw new RuntimeException();
            }
        }
        if(this.mypageMapper.updateReservationExpired(reservation) > 0 && this.mypageMapper.updatePaymentByTicketNumber(payment) > 0) {
            return CancelTicketResult.SUCCESS;
        } else {
            throw new RuntimeException();
        }

        /*
        return this.mypageMapper.updateReservationExpired(reservation) > 0 && this.mypageMapper.updatePaymentByTicketNumber(payment) > 0
                ? CancelTicketResult.SUCCESS
                : CancelTicketResult.FAILURE;

         */
    }

}
