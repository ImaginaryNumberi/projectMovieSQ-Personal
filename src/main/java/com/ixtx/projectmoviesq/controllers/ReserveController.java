package com.ixtx.projectmoviesq.controllers;

import com.ixtx.projectmoviesq.enums.ReserveResult;
import com.ixtx.projectmoviesq.services.ReserveService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping(value = "/reserve")
public class ReserveController {
    private final ReserveService reserveService;
    @Autowired
    public ReserveController(ReserveService reserveService) {
        this.reserveService = reserveService;
    }
    // 예매 페이지 원본(뼈대)

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getReserve() {
        ModelAndView modelAndView = new ModelAndView("/pages/reserve");
        return modelAndView;
    }

    // 예매 페이지(뼈대) 로드 후, 모든 영화관, 영화, 요일 리스트 불러와서 덫붙이는 곳
    @RequestMapping(value = "/theaterMovieTime",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getTheatersMoviesTimes() {
        JSONObject responseObject = new JSONObject();
        // 초기 모든 영화관, 영화, 요일 리스트 요소들
        responseObject.put("regionTheaterList", this.reserveService.getTheater());
        responseObject.put("movieList", this.reserveService.getMovie());
        responseObject.put("timeList", this.reserveService.getTime());
        return responseObject.toString();
    }


    // 영화관, 영화, 요일 공통값
    // 세개 다 골랐을시 상영관값 리스트 조회
    @RequestMapping(value = "/intersectionTheaterMovieTimeScreen",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getIntersectionTheatersMoviesTimes(@RequestParam(value = "theaterName", defaultValue = "") String theaterName,
                                                     @RequestParam(value = "movieName", defaultValue = "") String movieName,
                                                     @RequestParam(value = "timeName", defaultValue = "") String timeName) {
        JSONObject responseObject = new JSONObject();
        // 영화관 공통값
        responseObject.put("intersectionTheater", this.reserveService.getIntersectionTheater(movieName, timeName));
        // 영화 공통값
        responseObject.put("intersectionMovie", this.reserveService.getIntersectionMovie(theaterName, timeName));
        // 요일 공통값
        responseObject.put("intersectionTime", this.reserveService.getIntersectionTime(theaterName, movieName));
        // 세개값이 모두 있을시, 상영관 리스트 조회
        if (!theaterName.equals("") && !movieName.equals("") && !timeName.equals("")) {
            responseObject.put("screenList", this.reserveService.getScreen(theaterName, movieName, timeName));
        }
        return responseObject.toString();
    }


    // 인원/좌석 버튼 클릭시, 리다이렉트
    // 세션 유효 검사
    // 인덱스 값 유효성 검사
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public String postCheckSessionAndIndex(HttpServletRequest request,
                                           @RequestParam(value = "index") int index) {
        HttpSession session = request.getSession();
        JSONObject responseObject = new JSONObject();
        // 세션이 없을 때(로그인 하지 않았을 때)
        if(session.getAttribute("user") == null) {
            responseObject.put("result", ReserveResult.FAILURE_NOT_LOGIN.name().toLowerCase());
            return responseObject.toString();
        }
        // 인덱스 유효성 검사
        // 모든 좌석 매진 결과 검사
        ReserveResult result = this.reserveService.checkIndexAndSeat(index, session);
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();

        // 예시
        /*
        session.setAttribute("abc", "ㅋㅋㅋ");
        System.out.println(session.getAttribute("abc"));
        session.setAttribute("abc", "ㅇㅇㅇ");
        System.out.println(session.getAttribute("abc"));

        Enumeration enumeration = session.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            String sessionName = enumeration.nextElement().toString();
            String sessionValue = session.getAttribute(sessionName).toString();
            System.out.println(sessionName + " = " + sessionValue);
        }

        System.out.println(session.getAttribute("user") == null);
        if(session.getAttribute("user") != null) {
            UserEntity a = (UserEntity) session.getAttribute("user");
            System.out.println(a.getEmail());
        }
        System.out.println(session.getAttribute("user") == null);
        System.out.println("인덱스 : " + index);

         세션 관련 값 불러오기
        System.out.println(session.getAttribute("user")); // null 일시
        UserEntity a = (UserEntity) session.getAttribute("user");
        System.out.println(a.getEmail());
        System.out.println(index);

         redirect.addAttribute("index", index); // get방식으로 redirect 값 보내기
         redirect.addFlashAttribute("index", index);
         */
    }
}
