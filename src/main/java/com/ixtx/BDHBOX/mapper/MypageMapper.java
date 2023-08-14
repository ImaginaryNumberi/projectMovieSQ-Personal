package com.ixtx.BDHBOX.mapper;

import com.ixtx.projectmoviesq.dtos.TicketDto;
import com.ixtx.projectmoviesq.entities.PaymentEntity;
import com.ixtx.projectmoviesq.entities.ReservationEntity;
import com.ixtx.projectmoviesq.entities.ScheduleEntity;
import com.ixtx.projectmoviesq.entities.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;


@Mapper
public interface MypageMapper {

    // 회원여부 확인
    UserEntity selectUserByEmailPassword(@Param("email") String email, @Param("password") String password);

    // 회원 선택
    UserEntity selectUserByEmail(@Param("email") String email);

    // 회원 정보 수정
    int updateUserInformation(UserEntity user);

    // 회원 탈퇴 정보 수정
    int updateUserStatus(UserEntity user);

    // 예매 내역 조회
    TicketDto[] selectReservationByEmail(@Param("email") String email);

    // 예매 취소 내역 조회
    TicketDto[] selectCancelReservationByEmail(@Param("email") String email);

    // 해당 예매 내역 조회 
    ReservationEntity selectReservationByIndex(@Param("index") int index);

    // 관람 내역 조회
    TicketDto[] selectMovieLogByEmail(@Param("email") String email);

    // 예약 정보 조회
    TicketDto selectReservedInformationByIndex(@Param("index") int index);


    // 결제 정보 조회
    PaymentEntity selectPaymentByTicketNumber(@Param("ticketNumber") String ticketNumber);

    // 예매 취소 수정 
    int updateReservationExpired(ReservationEntity reservation);

    // 결제 취소 수정
    int updatePaymentByTicketNumber(PaymentEntity payment);

    ScheduleEntity selectScheduleByIndex(@Param("index") int index);


    // BDH WORK
    // 해당 좌석 지우기
    int deleteSeatNameInReservationStatus(@RequestParam("scheduleIndex") int scheduleIndex,
                                          @RequestParam("seatName") String seatName);

}
