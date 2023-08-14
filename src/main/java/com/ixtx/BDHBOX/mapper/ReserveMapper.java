package com.ixtx.BDHBOX.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReserveMapper {


    // 초기 모든 영화관, 영화, 요일 리스트 요소들
    List<Map<Object, Object>> selectTheaters();
    List<Map<Object, Object>> selectMovies();
    List<Map<Object, Object>> selectTimes();

    // 공통값 리스트 조회
    List<Map<Object, Object>> selectIntersectionTheater(String movieName, String timeName);
    List<Map<Object, Object>> selectIntersectionMovie(String theaterName, String timeName);
    List<Map<Object, Object>> selectIntersectionTime(String theaterName, String movieName);

    // 상영관 리스트 조회
    List<Map<Object, Object>> selectScreens(String theaterName, String movieName, String timeName);


    // 인덱스 유효검사
    // 잘못된 스케쥴 인덱스 입력 또는 이미 시간 지난 스케쥴 인덱스 조회를 걸러줌
    int selectSchedulessIndex(@RequestParam(value = "index") int index);

    // 전 좌석 예매 여부
    int selectCountOfSeat(@RequestParam(value = "index") int index);


}
