package com.ixtx.BDHBOX.mapper;

import com.ixtx.projectmoviesq.entities.PaymentEntity;
import com.ixtx.projectmoviesq.entities.ReservationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Mapper
public interface PaymentMapper {
    // KHR WORK
    Map<Object, Object> selectScheduleInformationByIndex(@Param("index") int index);
    // KHR WORK
    int insertPayment(PaymentEntity payment);
    // KHR WORK
    int  selectCardBinsByBiNumber(@Param("cardNumber") String cardNumber,
                                  @Param("company") String company);
    // KHR WORK
    Map<Object, Object> selectInformationByTicketNumber(@Param("ticketNumber")String ticketNumber);



    // BDH WORK
    // 세션인덱스로 스크린 인덱스 값 가져오기
    int selectScreenIndex(@RequestParam(value = "sessionIndex") int sessionIndex);
    // BDH WORK
    // 존재하지 않는 좌석값인지 아닌지 확인
    int selectExitSeat(@RequestParam(value = "sessionIndex") int sessionIndex,
                       @RequestParam(value = "seatName") String seatName);
    // BDH WORK
    // reservations 테이블 insert
    int insertReservationToReservations(ReservationEntity reservation);
    // BDH WORK
    // reservation_status 테이블 insert
    // Map, parameter 전달 방법
    // int insertReservationValueToReservationStatus(@RequestParam(value = "map") Map<String, Object> map);
    // No parameter 및 @RequestParam 전달 방법
    int insertReservationValueToReservationStatus(@RequestParam(value = "sessionIndex") int sessionIndex,
                                                  @RequestParam(value = "sessionScreenIndex") int sessionScreenIndex,
                                                  @RequestParam(value = "seatName") String seatName);
}
