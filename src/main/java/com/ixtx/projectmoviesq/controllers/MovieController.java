package com.ixtx.projectmoviesq.controllers;

import com.ixtx.projectmoviesq.dtos.MovieDto;
import com.ixtx.projectmoviesq.entities.MovieImageEntity;
import com.ixtx.projectmoviesq.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/movie")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // movie grid type list
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMovie(@RequestParam(value = "sort", defaultValue = "date", required = false) String sort) {
        ModelAndView modelAndView = new ModelAndView("movie/movie");
        MovieDto[] movieDtos = this.movieService.getMovies(sort);
        modelAndView.addObject("sort", sort);
        modelAndView.addObject("movies", movieDtos);
        return modelAndView;
    }

    // 영화 이미지 가져오기
    // movie.html에서 호출
    @RequestMapping(value = "image", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getMovieImage(@RequestParam("index") int index,
                                                @RequestParam(value = "poster",
                                                        defaultValue = "true",
                                                        required = false) boolean isPoster) {
        MovieImageEntity movieImage = this.movieService.getImage(index, isPoster);
        ResponseEntity<byte[]> response;

        if (movieImage == null) {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(movieImage.getImageData().length);
            headers.setContentType(MediaType.parseMediaType(movieImage.getImageType()));
            response = new ResponseEntity<>(movieImage.getImageData(), headers, HttpStatus.OK);
        }
        return response;
    }

    // movie detail
    @RequestMapping(value = "detail",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMovieDetail(@RequestParam("index") int index) {
        ModelAndView modelAndView = new ModelAndView("movie/detail");
        MovieDto movie = this.movieService.getMovieByIndex(index);
        modelAndView.addObject("movie", movie);
        return modelAndView;
    }
}