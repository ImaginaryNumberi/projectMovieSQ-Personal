package com.ixtx.projectmoviesq.controllers;

import com.ixtx.projectmoviesq.dtos.MovieDto;
import com.ixtx.projectmoviesq.dtos.NoticeDto;
import com.ixtx.projectmoviesq.entities.*;
import com.ixtx.projectmoviesq.models.PagingModel;
import com.ixtx.projectmoviesq.services.AdminService;
import com.ixtx.projectmoviesq.services.MovieService;
import com.ixtx.projectmoviesq.services.ScheduleService;
import com.ixtx.projectmoviesq.services.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private final AdminService adminService;
    private final MovieService movieService;
    private final SupportService supportService;
    private final ScheduleService scheduleService;

    @Autowired
    public AdminController(AdminService adminService, MovieService movieService, SupportService supportService, ScheduleService scheduleService) {
        this.adminService = adminService;
        this.movieService = movieService;
        this.supportService = supportService;
        this.scheduleService = scheduleService;
    }


    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAdmin() {
        return new ModelAndView("/admin/index");
    }

    // admin > movie
    @RequestMapping(value = "movie",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAdminMovie() {
        ModelAndView modelAndView = new ModelAndView("/admin/manageMovie");
        MovieEntity[] movies = this.adminService.getMovieList();
        modelAndView.addObject("total", String.valueOf(movies.length));
        modelAndView.addObject("movies", movies);
        return modelAndView;
    }

    // 영화 등록 페이지 :  /admin/registerMovie
    @RequestMapping(value = "registerMovie",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAdminRegisterMovie() {
        return new ModelAndView("/admin/registerMovie");
    }

    // 영화 등록 : html에서 form으로 입력받아서 등록
    @RequestMapping(value = "registerMovie",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postAdminRegisterMovie(HttpServletRequest request, MovieEntity movie, @RequestParam(value = "category") String category, MultipartFile[] file) throws IOException {
        if (file == null) {
            file = new MultipartFile[0];
        }
        boolean result = this.adminService.putMovieInformation(request, movie, category, file);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/registerMovie");
        modelAndView.addObject("result", result);
        return modelAndView;
    }

    // 영화 수정 페이지 :  /admin/updateMovie
    @RequestMapping(value = "modifyMovie",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAdminModifyMovie(@RequestParam(value = "index") int index) {
        ModelAndView modelAndView = new ModelAndView("/admin/modifyMovie");
        MovieDto movie = this.movieService.getMovieByIndex(index);
        int movieImageIndex = this.movieService.getImage(index, true).getIndex();
        modelAndView.addObject("movie", movie);
        modelAndView.addObject("movieImageIndex", movieImageIndex);
        return modelAndView;
    }

    // 영화 수정 : html에서 form으로 입력받아서 등록
    @RequestMapping(value = "modifyMovie",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postAdminModifyMovie(HttpServletRequest request, MovieEntity movie, @RequestParam(value = "category") String category, @RequestParam(value = "movieImageIndex", defaultValue = "0", required = false) String movieImageIndex, MultipartFile[] file) throws IOException {
        if (file == null) {
            file = new MultipartFile[0];
        }

        boolean result = this.adminService.updateMovieInformation(request, movie, category, Integer.parseInt(movieImageIndex), file);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/modifyMovie");
        modelAndView.addObject("movie", movie);
        modelAndView.addObject("movieImageIndex", this.movieService.getImage(movie.getIndex(), true).getIndex());
        modelAndView.addObject("result", result);
        return modelAndView;
    }

    // 영화 이미지 추가 페이지 :  /admin/registerMovieImage
    @RequestMapping(value = "registerMovieImage",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAdminRegisterMovieImage() {
        ModelAndView modelAndView = new ModelAndView("/admin/registerMovieImage");
        MovieDto[] movieDtos = this.movieService.getMovies("abc");
        modelAndView.addObject("movies", movieDtos);
        return modelAndView;
    }

    // 영화 이미지 추가 : html에서 form으로 입력받아서 등록
    @RequestMapping(value = "registerMovieImage",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postAdminRegisterMovieImage(HttpServletRequest request, @RequestParam(value = "index") int index, @RequestParam(value = "category") String category, MultipartFile file) throws IOException {
        boolean result = this.adminService.putMovieImage(request, index, category, file);
        ModelAndView modelAndView = new ModelAndView("admin/registerMovieImage");
        MovieDto[] movieDtos = this.movieService.getMovies("abc");
        modelAndView.addObject("movies", movieDtos);
        modelAndView.addObject("result", result);
        return modelAndView;
    }

    // admin > support
    @RequestMapping(value = "support", method = RequestMethod.GET)
    public ModelAndView getAdminSupport(@RequestParam(value = "p", defaultValue = "1", required = false) int requestPage,
                                        @RequestParam(value = "c", defaultValue = "content", required = false) String searchCriterion,
                                        @RequestParam(value = "q", defaultValue = "", required = false) String searchQuery) {
        ModelAndView modelAndView = new ModelAndView("admin/manageSupport");
        PagingModel pagingModel = new PagingModel(
                supportService.PAGE_COUNT,
                this.supportService.getNoticeCount(searchCriterion, searchQuery),
                requestPage);
        List<NoticeDto> noticeDtos = this.supportService.getNoticeByPage(pagingModel, searchCriterion, searchQuery);
        modelAndView.addObject("notices", noticeDtos);
        modelAndView.addObject("total", String.valueOf(this.supportService.getNoticeCount("", "")));
        modelAndView.addObject("pagingModel", pagingModel);
        return modelAndView;
    }

    // 삭제
    @RequestMapping(value = "support", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteAdminSupport(@RequestParam(value = "index") int index) {
        boolean result = this.adminService.deleteNoticeByIndex(index);
        return String.valueOf(result);
    }

    // 공지사항 등록 페이지 : /admin/registerNotice
    @RequestMapping(value = "registerNotice",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAdminRegisterNotice() {
        return new ModelAndView("admin/registerNotice");
    }

    // 공지사항 등록 : html에서 form으로 입력받아서 등록
    @RequestMapping(value = "registerNotice",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postAdminRegisterNotice(
            @RequestParam(value = "category") String category,
            HttpServletRequest request,
            HttpSession session,
            NoticeEntity notice,
            MultipartFile[] file) throws IOException {
        if (file == null) {
            file = new MultipartFile[0];
        }
        boolean result = this.adminService.putNotice(request, session, notice, category, file);
        ModelAndView modelAndView = new ModelAndView();
        if (result) {
            modelAndView.addObject("redirect:/support/notice?index=" + notice.getIndex());
        } else {
            modelAndView.setViewName("admin/registerNotice");
            modelAndView.addObject("result", result);
        }
        return modelAndView;
    }

    // 영화 뉴스 등록 페이지 :  /admin/registerNews
    @RequestMapping(value = "registerNews",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAdminRegisterNews() {
        ModelAndView modelAndView = new ModelAndView("/admin/registerNews");
        return modelAndView;
    }

    // 영화 뉴스 등록 : html에서 form으로 입력받아서 등록
    @RequestMapping(value = "registerNews",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postAdminRegisterNews(HttpServletRequest request, NewsEntity news, MultipartFile file) throws IOException {
        boolean result = this.adminService.putMovieNews(request, news, file);
        ModelAndView modelAndView = new ModelAndView("admin/registerNews");
        return modelAndView;
    }

    // 광고 등록 화면 : /admin/registerCommercial
    @RequestMapping(value = "registerCommercial",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRegisterCommercial(@SessionAttribute(value="user")UserEntity user) {
        if(user == null ){
            return new ModelAndView("redirect:./login");
        }
        if (!user.isAdmin()){
            return new ModelAndView("redirect:../");
        }
        ModelAndView modelAndView = new ModelAndView("admin/registerCommercial");
        // 영화 목록 보이기
        List<MovieDto> movieDtos = this.adminService.getMovies();
        modelAndView.addObject("movies", movieDtos);
        return modelAndView;
    }

    @RequestMapping(value = "manageCommercial",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getManageCommercial(@SessionAttribute(value="user")UserEntity user) {
        if(user == null ){
            return new ModelAndView("redirect:./login");
        }
        if (!user.isAdmin()){
            return new ModelAndView("redirect:../");
        }
        ModelAndView modelAndView = new ModelAndView("admin/manageCommercial");
        // 영화 목록 보이기
        List<MovieDto> movieDtos = this.adminService.getMovies();
        modelAndView.addObject("movies", movieDtos);
        // 광고 목록 보이기
        CommercialEntity[] commercials = this.adminService.getCommercialList();
        modelAndView.addObject("total", String.valueOf(commercials.length));
        modelAndView.addObject("commercials", commercials);
        return modelAndView;

    }

    // 광고 수정 페이지 : /admin/modifyCommercial=index?
    @RequestMapping(value = "modifyCommercial",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getModifyCommercial(@RequestParam(value = "index") int index) {
        CommercialEntity commercial = this.adminService.getCommercialByIndex(index);
        MovieDto movie = this.movieService.getMovieByIndex(commercial.getMovieIndex());
        ModelAndView modelAndView = new ModelAndView("admin/modifyCommercial");
        modelAndView.addObject("commercial", commercial);
        modelAndView.addObject("movie", movie);
        return modelAndView;
    }

    // 광고 정보 수정
    @RequestMapping(value = "modifyCommercial",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postModifyCommercial(CommercialEntity commercial,
                                             @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                                             @RequestParam(value = "commercialName") String commercialName,
                                             @RequestParam(value = "startDate") String startDate,
                                             @RequestParam(value = "finishDate") String finishDate,
                                             @RequestParam(value = "commercialIndex") int index) throws ParseException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse(startDate);
        commercial.setStartedAt(start);
        Date finish = sdf.parse(finishDate);
        commercial.setFinishedAt(finish);
        commercial.setCommercialName(commercialName);
        boolean result = this.adminService.updateCommercialInformation(index, commercial, multipartFile);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/modifyCommercial");
        MovieDto movie = this.movieService.getMovieByIndex(commercial.getMovieIndex());
        modelAndView.addObject("commercial", this.adminService.getCommercialByIndex(index));
        modelAndView.addObject("movie", movie);
        modelAndView.addObject("index", commercial.getIndex());
        System.out.println(String.valueOf(result));
        modelAndView.addObject("result", String.valueOf(result));
        return modelAndView;
    }

    // 광고 등록 : html에서 form으로 입력받아서 등록
    @RequestMapping(value = "registerCommercial",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postRegisterCommercial(
            @RequestParam(value = "movie") String index,
            @RequestParam(value = "commercialName") String commercialName,
            CommercialEntity commercial,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "finishDate") String finishDate,
            @RequestParam(value = "file") MultipartFile multipartFile) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse(startDate);
        commercial.setStartedAt(start);
        Date finish = sdf.parse(finishDate);
        commercial.setFinishedAt(finish);
        boolean result = this.adminService.putCommercial(index, commercialName, commercial, multipartFile);
        ModelAndView modelAndView = new ModelAndView();
        if (result) {
            modelAndView.addObject("redirect:/admin/registerCommercial");
            List<MovieDto> movieDtos = this.adminService.getMovies();
            modelAndView.addObject("movie", movieDtos);
            CommercialEntity[] commercials = this.adminService.getCommercialList();
            modelAndView.addObject("commercials", commercials);
        } else {
            modelAndView.setViewName("admin/registerCommercial");
        }
        return modelAndView;
    }


    // 광고 상태 수정
    @ResponseBody
    @RequestMapping(value = "commercial",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String patchCommercial(@RequestParam(value = "result") String dataResult) throws ParseException {
        if (dataResult.length() == 2) {
            return "none";
        }
        boolean result = this.adminService.updateCommercial(dataResult);
        return String.valueOf(result);
    }

    // 광고 목록에서 광고 삭제
    @ResponseBody
    @RequestMapping(value = "commercialList",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String patchCommercial(@RequestParam(value = "index") int index) {
        boolean result = this.adminService.deleteCommercial(index);
        return String.valueOf(result);
    }

    //스케쥴 관리 페이지
    @RequestMapping(value = "manageSchedule", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMangeSchedule() {
        return new ModelAndView("/admin/manageSchedule");
    }

    //스케쥴 불러옴
    @RequestMapping(value = "ScheduleList", method = RequestMethod.GET)
    public ResponseEntity<List<ScheduleEntity>> getAllSchedules() {
        List<ScheduleEntity> Schedule = scheduleService.getAllSchedules(); // 인스턴스에서 비정적 메소드 호출
        return ResponseEntity.ok(Schedule);
    }

    //스케쥴 추가
    @RequestMapping(value = "addSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addSchedule(@RequestBody ScheduleEntity schedule) {
        try {
            scheduleService.addSchedule(schedule);
            return ResponseEntity.ok("추가 성공");
        } catch (Exception e) {
            return new ResponseEntity<>("추가 실패 " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //스케쥴 삭제
    @RequestMapping(value = "deleteSchedule", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteSchedule(@RequestParam(value = "index") int index) {
        boolean result = this.scheduleService.deleteSchedule(index);
        return String.valueOf(result);
    }

    //스케쥴 추가
    @RequestMapping(value = "updateSchedule", method = RequestMethod.PATCH)
    public ResponseEntity<ScheduleEntity> updateSchedule(@RequestBody ScheduleEntity scheduleEntity) {
        ScheduleEntity updatedSchedule = this.scheduleService.updateSchedule(scheduleEntity);
        if (updatedSchedule != null) {
            return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}