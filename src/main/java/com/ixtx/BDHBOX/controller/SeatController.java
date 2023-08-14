package com.ixtx.BDHBOX.controller;

import com.ixtx.projectmoviesq.dtos.MinimapDto;
import com.ixtx.projectmoviesq.dtos.SeatDto;
import com.ixtx.projectmoviesq.enums.SeatResult;
import com.ixtx.projectmoviesq.services.SeatService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Controller
@RequestMapping(value = "/seat")
public class SeatController {

    private final SeatService seatService;
    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }


    //seat페이지 뼈대
    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postSeat(HttpServletRequest request,
                                 @RequestParam(value = "index") int index){

        // 로그인 판별
        // 전 좌석 예매 되었을 때
        // 세션값과 인덱스 값이 같은지 확인
        HttpSession session = request.getSession();
        ModelAndView modelAndView = new ModelAndView("/pages/seat");


        // 세션이 없을 때(로그인 하지 않았을 때)
        // 주의점) 로그인창에서 로그인해서 '다시 이 페이지로 들어오면 안된다'
        if(session.getAttribute("user") == null) {
            // 주의점) 로그인창에서 로그인해서 '다시 이 페이지로 들어오면 안된다'
            // 주의점) 로그인창에서 로그인해서 '다시 이 페이지로 들어오면 안된다'
            // 주의점) 로그인창에서 로그인해서 '다시 이 페이지로 들어오면 안된다'
            session.removeAttribute("user");
            session.removeAttribute("index");
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        // 세션인덱스값이 null일때(reserve 페이지를 거치지 않고 어떠한 우회적인 방법으로 페이지 접속 했을 경우)
        if (session.getAttribute("index") == null) {
            //세션 인덱스 및 로그인 초기화
            session.removeAttribute("user");
            session.removeAttribute("index");
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        // reserve에서 세션값과 받아온 인덱스값이 일치하지 않을 때
        // 해당 세션인덱스 값과 index값을 비교하여 변조를 막음
        int sessionIndex = (int) session.getAttribute("index");
        if(sessionIndex != index) {
            //세션 인덱스 초기화
            session.removeAttribute("user");
            session.removeAttribute("index");
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }


        //상세정보
        //등급
        Map<Object, Object> info = this.seatService.getInfo(index);
        modelAndView.addObject("info", info.get("information"));
        modelAndView.addObject("rating",info.get("rating"));


        // 미니맵 존재 여부
        MinimapDto existence = this.seatService.getImage(index);
        if(existence.getSeatsMap() != null && existence.getMapType() != null) {
            modelAndView.addObject("minimap", 1);
        } else {
            modelAndView.addObject("minimap", 0);
        }

        // 15분뒤 인덱스세션 자동 삭제
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                session.removeAttribute("index");
            }
        };
        timer.schedule(timerTask, 900000); //1000 = 1초
        modelAndView.addObject("index", index);
        return modelAndView;
    }


    //미니맵 불러오기
    // Entity가아닌 Dto 객체로 활용
    @RequestMapping(value = "/minimap",
            method = RequestMethod.GET)
    public ResponseEntity<byte[]> getMinimap(@RequestParam("index") int index) {
        MinimapDto screenMinimap = this.seatService.getImage(index);
        ResponseEntity<byte[]> response;
        if(screenMinimap == null) {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(screenMinimap.getSeatsMap().length);
            headers.setContentType(MediaType.parseMediaType(screenMinimap.getMapType()));
            response = new ResponseEntity<>(screenMinimap.getSeatsMap(), headers, HttpStatus.OK);
        }
        return response;
    }


    //페이지 로드 다되었을때
    // 좌석 및 예약좌석 정보 불러오기
    @RequestMapping(value = "/selectSeats",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SeatDto[] getSeat(HttpServletRequest request) {
        HttpSession session = request.getSession();
        int index = (int) session.getAttribute("index");
        return this.seatService.getSeats(index);
    }


    // 존재하지 않는 좌석값인지 아닌지 확인
    // 좌석 버튼 클릭시, 예약이 되어있는지 또 한번 조회
    // 콜백 관련 콜백 관련 콜백 관련 콜백 관련 콜백 관련 콜백 관련
    @RequestMapping(value = "/checkStatusSeat", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getStatusSeat(HttpServletRequest request,
                                @RequestParam("seatName") String seatName) {
        JSONObject responseObject = new JSONObject();
        HttpSession session = request.getSession();
        // 세션인덱스값이 없을 시
        if (session.getAttribute("index") == null) {
            // reserve 페이지로 돌아가야함
            responseObject.put("result", SeatResult.EXPIRED_SESSIONINDEX.name().toLowerCase());
            return responseObject.toString();
        }
        int sessionIndex = (int) session.getAttribute("index");
        // 존재하지 않는 좌석값인지 아닌지 확인
        // 좌석 버튼 클릭시, 예약이 되어있는지 또 한번 조회
        SeatResult result = this.seatService.getStatus(sessionIndex, seatName);
        if(result == SeatResult.NONEXISTENT_SEAT) {
            // 강제 로그아웃
            session.removeAttribute("user");
            session.removeAttribute("index");
            responseObject.put("result", result.name().toLowerCase());
            return responseObject.toString();
        } else if (result == SeatResult.RESERVED_SEAT) {
            responseObject.put("result", result.name().toLowerCase());
            return responseObject.toString();
        }
        // 빈좌석일시
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }
}
