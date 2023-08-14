package com.ixtx.projectmoviesq.controllers;

import com.ixtx.projectmoviesq.dtos.NoticeDto;
import com.ixtx.projectmoviesq.entities.NewsEntity;
import com.ixtx.projectmoviesq.models.PagingModel;
import com.ixtx.projectmoviesq.services.SupportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "support")
public class SupportController {
    private final SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView getSupport
            (@RequestParam(value = "p", defaultValue = "1", required = false) int requestPage,
             @RequestParam(value = "c", defaultValue = "content", required = false) String searchCriterion,
             @RequestParam(value = "q", defaultValue = "", required = false) String searchQuery) {
        ModelAndView modelAndView = new ModelAndView("support/notice");
        PagingModel noticePagingModel = new PagingModel(
                supportService.PAGE_COUNT,
                this.supportService.getNoticeCount(searchCriterion, searchQuery),
                requestPage);
        List<NoticeDto> noticeDtos = this.supportService.getNoticeByPage(noticePagingModel, searchCriterion, searchQuery);
        modelAndView.addObject("notices", noticeDtos);
        modelAndView.addObject("pagingModel", noticePagingModel);
        modelAndView.addObject("searchCriterion", searchCriterion);
        modelAndView.addObject("searchQuery", searchQuery);
        return modelAndView;
    }

    @RequestMapping(value = "faq", method = RequestMethod.GET)
    public ModelAndView getSupportFaq(@RequestParam(value = "p", defaultValue = "1", required = false) int requestPage,
                                      @RequestParam(value = "c", defaultValue = "content", required = false) String searchCriterion,
                                      @RequestParam(value = "q", defaultValue = "", required = false) String searchQuery) {
        ModelAndView modelAndView = new ModelAndView("support/faq");
        PagingModel faqPagingModel = new PagingModel(
                supportService.PAGE_COUNT,
                this.supportService.getFaqCount(searchCriterion, searchQuery),
                requestPage);
        List<NoticeDto> faqDtos = this.supportService.getFaqByPage(faqPagingModel, searchCriterion, searchQuery);
        modelAndView.addObject("faqs", faqDtos);
        modelAndView.addObject("faqPagingModel", faqPagingModel);
        modelAndView.addObject("faqSearchCriterion", searchCriterion);
        modelAndView.addObject("faqSearchQuery", searchQuery);
        return modelAndView;
    }

    // 공지사항 자세히 보기
    @RequestMapping(value = "detail",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView getSupportDetail(@RequestParam(value = "index") int index) {
        ModelAndView modelAndView = new ModelAndView("support/noticeDetail");
        NoticeDto noticeDto = this.supportService.getNotice(index);
        modelAndView.addObject("title", noticeDto.getTitle());
        modelAndView.addObject("view", noticeDto.getView());
        modelAndView.addObject("createdOn", noticeDto.getCreatedOn());
        modelAndView.addObject("content", noticeDto.getContent());
        return modelAndView;
    }

    // 영화 소식
    @RequestMapping(value = "news",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMovieNews(@RequestParam(value = "p", defaultValue = "1", required = false) int requestPage) {
        ModelAndView modelAndView = new ModelAndView("support/news");
        PagingModel newsPagingModel = new PagingModel(supportService.PAGE_COUNT_NEWS,
                this.supportService.getNewsCount(), requestPage);
        NewsEntity[] newsList = this.supportService.getNewsByPage(newsPagingModel);
        modelAndView.addObject("newsList", newsList);
        modelAndView.addObject("pagingModel", newsPagingModel);
        return modelAndView;
    }

    // 영화 소식 이미지 가져오기
    @RequestMapping(value = "newsImage", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getMovieImage(
            @RequestParam(value = "index", required = false) int index,
            @RequestParam(value = "news", defaultValue = "true", required = false) boolean isNews) {
        NewsEntity news = this.supportService.getMovieNewsByIndex(index);
        ResponseEntity<byte[]> response;
        if (news == null) {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(news.getImageData().length);
            headers.setContentType(MediaType.parseMediaType(news.getImageType()));
            response = new ResponseEntity<>(news.getImageData(), headers, HttpStatus.OK);
        }
        return response;
    }

    // 영화 소식 자세히 보기
    @RequestMapping(value = "news/detail", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getSupportNewsDetail(@RequestParam(value = "index") int index) {
        ModelAndView modelAndView = new ModelAndView("support/newsDetail");
        NewsEntity news = this.supportService.getMovieNewsByIndex(index);
        modelAndView.addObject("news", news);
        return modelAndView;
    }
}
