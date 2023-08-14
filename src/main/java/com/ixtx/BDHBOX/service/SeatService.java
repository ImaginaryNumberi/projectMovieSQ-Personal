package com.ixtx.BDHBOX.service;

import com.ixtx.projectmoviesq.dtos.MinimapDto;
import com.ixtx.projectmoviesq.dtos.SeatDto;
import com.ixtx.projectmoviesq.enums.SeatResult;
import com.ixtx.projectmoviesq.mappers.SeatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SeatService {

    private final SeatMapper seatMapper;
    @Autowired
    public SeatService(SeatMapper seatMapper) {
        this.seatMapper = seatMapper;
    }



    //상세정보
    public Map<Object, Object> getInfo(int index){
        return this.seatMapper.selectInformation(index);
    }
    //미니맵 불러오기
    public MinimapDto getImage(int index) {
        return this.seatMapper.selectScreenImage(index);
    }

    // 좌석 및 예약좌석 정보 불러오기
    public SeatDto[] getSeats(int index) {
        return this.seatMapper.selectAllSeatAndReservedSeat(index);
    }

    // 존재하지 않는 좌석값인지 아닌지 확인
    // 좌석 버튼 클릭시, 예약이 되어있는지 또 한번 조회
    public SeatResult getStatus(int sessionIndex, String seatName) {
        // 존재하지 않는 좌석값인지 아닌지 확인
        if (this.seatMapper.selectExitSeat(sessionIndex, seatName) == 0) {
            return SeatResult.NONEXISTENT_SEAT;
        }
        // 도중에 예약된 좌석인지 확인
        if (this.seatMapper.selectStatusSeat(sessionIndex, seatName) == 1) {
            return SeatResult.RESERVED_SEAT;
        }
        // 빈좌석
        return SeatResult.AVAILABLE_SEAT;
    }
}