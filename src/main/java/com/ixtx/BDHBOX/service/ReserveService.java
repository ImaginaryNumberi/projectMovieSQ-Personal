package com.ixtx.BDHBOX.service;

import com.ixtx.projectmoviesq.enums.ReserveResult;
import com.ixtx.projectmoviesq.mappers.ReserveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReserveService {


    private final ReserveMapper reserveMapper;
    @Autowired
    public ReserveService(ReserveMapper reserveMapper) {
        this.reserveMapper = reserveMapper;
    }


    // 예매 페이지 로드 후, 영화관, 영화, 요일 리스트 불러오는 곳
    public List<Map<Object, Object>> getTheater() {
        return this.reserveMapper.selectTheaters();
    }
    public List<Map<Object, Object>> getMovie() {
        return this.reserveMapper.selectMovies();
    }
    public List<Map<Object, Object>> getTime() {
        return this.reserveMapper.selectTimes();
    }



    // 영화관 공통값
    public List<Map<Object, Object>> getIntersectionTheater(String movieName, String timeName) {
        List<Map<Object, Object>> intersectionTheater = new ArrayList<>();
        // 페이지에서 가져온 값이 둘다 비었을 경우
        if (movieName.equals("") && timeName.equals("")) {
            Map<Object, Object> nothingElement = new HashMap<>() {{
                put("nothingElement", "true");
            }};
            intersectionTheater.add(0, nothingElement);
            return intersectionTheater;
        }
        intersectionTheater = this.reserveMapper.selectIntersectionTheater(movieName, timeName);
        // 처음 페이지 로드할땐 DB값이 존재했지만 시간이 지났거나 어떠한 이유로 DB값이 삭제 또는 조회가 안될 경우
        if (intersectionTheater.size() == 0) {
            Map<Object, Object> expiredElement = new HashMap<>() {{
                put("expiredElement", "true");
            }};
            intersectionTheater.add(0, expiredElement);
            return intersectionTheater;
        }
        return intersectionTheater;
    }
    // 영화 공통값
    public List<Map<Object, Object>> getIntersectionMovie(String theaterName, String timeName) {
        List<Map<Object, Object>> intersectionMovie = new ArrayList<>();
        // 페이지에서 가져온 값이 둘다 비었을 경우
        if (theaterName.equals("") && timeName.equals("")) {
            Map<Object, Object> nothingElement = new HashMap<>() {{
                put("nothingElement", "true");
            }};
            intersectionMovie.add(0, nothingElement);
            return intersectionMovie;
        }
        intersectionMovie = this.reserveMapper.selectIntersectionMovie(theaterName, timeName);
        // 처음 페이지 로드할땐 DB값이 존재했지만 시간이 지났거나 어떠한 이유로 DB값이 삭제 또는 조회가 안될 경우
        if (intersectionMovie.size() == 0) {
            Map<Object, Object> expiredElement = new HashMap<>() {{
                put("expiredElement", "true");
            }};
            intersectionMovie.add(0, expiredElement);
            return intersectionMovie;
        }
        return intersectionMovie;
    }
    // 요일 공통값
    public List<Map<Object, Object>> getIntersectionTime(String theaterName, String movieName) {
        List<Map<Object, Object>> intersectionTime = new ArrayList<>();
        // 페이지에서 가져온 값이 둘다 비었을 경우
        if (theaterName.equals("") && movieName.equals("")) {
            Map<Object, Object> nothingElement = new HashMap<>() {{
                put("nothingElement", "true");
            }};
            intersectionTime.add(0, nothingElement);
            return intersectionTime;
        }
        intersectionTime = this.reserveMapper.selectIntersectionTime(theaterName, movieName);
        // 처음 페이지 로드할땐 DB값이 존재했지만 시간이 지났거나 어떠한 이유로 DB값이 삭제 또는 조회가 안될 경우
        if (intersectionTime.size() == 0) {
            Map<Object, Object> expiredElement = new HashMap<>() {{
                put("expiredElement", "true");
            }};
            intersectionTime.add(0, expiredElement);
            return intersectionTime;
        }
        return intersectionTime;
    }


    // 상영관 리스트 조회
    public List<Map<Object, Object>> getScreen(String theaterName, String movieName, String timeName) {
        List<Map<Object, Object>> screens = this.reserveMapper.selectScreens(theaterName, movieName, timeName);
        // null은 언제 뜨는가? 리스트는 null이 뜨지 않으므로 if(screens.size() == 0) {} 써야함이 옳다
        // 처음 페이지 로드할땐 DB값이 존재했지만 시간이 지났거나 어떠한 이유로 DB값이 삭제 또는 조회가 안될 경우
        if (screens.size() == 0) {
            Map<Object, Object> expiredElement = new HashMap<>() {{
                put("expiredElement", "true");
            }};
            screens.add(0, expiredElement);
            return screens;
        }
        return screens;
    }



    // 로그인 판별
    // 인덱스 유효성 검사
    // 전좌석 예매 여부
    public ReserveResult checkIndexAndSeat (int index, HttpSession session) {
        // 인덱스 유효성 검사
        if(this.reserveMapper.selectSchedulessIndex(index) != 1) {
            return ReserveResult.FAILURE_INVALID_INDEX;
        }
        // 전 좌석 예매 검사
        if(this.reserveMapper.selectCountOfSeat(index) == 1) {
            return ReserveResult.FAILURE_FULL_SEAT;
        }
        // seat 페이지에서 해당 세션인덱스 값과 index값을 비교하여 변조를 막음
        session.setAttribute("index", index);
        return ReserveResult.SUCCESS;
    }
}
