package com.ixtx.projectmoviesq.controllers;

import com.ixtx.projectmoviesq.dtos.MovieDto;
import com.ixtx.projectmoviesq.entities.CommercialEntity;
import com.ixtx.projectmoviesq.entities.NewsEntity;
import com.ixtx.projectmoviesq.entities.NoticeEntity;
import com.ixtx.projectmoviesq.enums.CommonResult;
import com.ixtx.projectmoviesq.services.AdminService;
import com.ixtx.projectmoviesq.services.MovieService;
import com.ixtx.projectmoviesq.services.SupportService;
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/")
public class HomeController {

    private final AdminService adminService;
    private final MovieService movieService;
    private final SupportService supportService;

    @Autowired
    public HomeController(AdminService adminService, MovieService movieService, SupportService supportService) {
        this.adminService = adminService;
        this.movieService = movieService;
        this.supportService = supportService;
    }

    // 메인화면
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndex() {
        ModelAndView modelAndView = new ModelAndView("home/index");
        //광고
        CommercialEntity[] commercials = this.adminService.getCommercials();
        modelAndView.addObject("commercials", commercials);
        //top10 영화 (상영중, 상영예정) -> 예매율 순으로 해야함(-)
        List<MovieDto> movieDtos = this.movieService.getMoviesByReservationRate();
        modelAndView.addObject("movies", movieDtos);
        // 공지사항
        NoticeEntity[] notices = this.supportService.getByRecent();
        modelAndView.addObject("notices", notices);
        //영화소식
        NewsEntity[] news = this.supportService.getMovieLatestNews();
        modelAndView.addObject("newsList", news);
        return modelAndView;
    }

    // 광고 사진 가져오기
    @RequestMapping(value = "commercialImage", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getCommercialImage(@RequestParam("index") int index) {
        CommercialEntity commercial = this.adminService.getCommercialImage(index);
        ResponseEntity<byte[]> response;
        if (commercial == null) {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            response = new ResponseEntity<>(commercial.getImageData(), headers, HttpStatus.OK);
        }
        return response;
    }

    // url 가져오기
    @ResponseBody
    @RequestMapping(value = "url", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMovieUrl(@RequestParam(value = "index") int index) {
        MovieDto movie = this.movieService.getMovieByIndex(index);
        JSONObject responseObject = new JSONObject();
        if(movie != null){
            responseObject.put("url", movie.getTrailerUrl());
            responseObject.put("result", CommonResult.SUCCESS.name().toLowerCase());
        } else {
            responseObject.put("result",CommonResult.FAILURE.name().toLowerCase());
        }
        return responseObject.toString();
    }
}