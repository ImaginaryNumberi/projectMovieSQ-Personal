package com.ixtx.projectmoviesq.mappers;

import com.ixtx.projectmoviesq.dtos.MinimapDto;
import com.ixtx.projectmoviesq.dtos.SeatDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Mapper
public interface SeatMapper {


    //상세정보
    Map<Object, Object> selectInformation(@Param(value = "index") int index);
    // 미니맵 불러오기
    MinimapDto selectScreenImage(@Param("index") int index);

    // 좌석 및 예약좌석 정보 불러오기
    SeatDto[] selectAllSeatAndReservedSeat(@RequestParam(value = "index") int index);


    // 존재하지 않는 좌석값인지 아닌지 확인
    int selectExitSeat(@RequestParam(value = "sessionIndex") int sessionIndex,
                       @RequestParam(value = "seatName") String seatName);

    // 좌석 버튼 클릭시, 예약이 되어있는지 또 한번 조회
    int selectStatusSeat(@RequestParam(value = "sessionIndex") int sessionIndex,
                         @RequestParam(value = "seatName") String seatName);


}