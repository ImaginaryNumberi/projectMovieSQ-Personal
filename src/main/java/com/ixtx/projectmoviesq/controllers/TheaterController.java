package com.ixtx.projectmoviesq.controllers;

import com.ixtx.projectmoviesq.entities.TheaterEntity;
import com.ixtx.projectmoviesq.services.TheaterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/theater")
public class TheaterController {
    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }
    // 영화관 목록

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getTheaterList() {
        ModelAndView modelAndView = new ModelAndView("/pages/theater");
        TheaterEntity[] theaters = this.theaterService.getTheaters();
        modelAndView.addObject("theaters", theaters);

        TheaterEntity[] regions = this.theaterService.getTheaterRegion();
        modelAndView.addObject("regions", regions);

        return modelAndView;
    }

    @RequestMapping(value = "/theaterList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TheaterEntity[]> getTheater() {
        TheaterEntity[] theaters = this.theaterService.getTheaters();
        return ResponseEntity.ok(theaters);
    }

    @RequestMapping(value = "/regionsList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TheaterEntity[]> getRegions() {
        TheaterEntity[] regions = this.theaterService.getTheaterRegion();
        return ResponseEntity.ok(regions);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/theaterImage", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getTheaterImage(@RequestParam String theaterName) {
        String image = this.theaterService.getTheaterImage(theaterName);
        return ResponseEntity.ok(image);
    }


    @RequestMapping(value = "theaterPage", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getTheaterPage() {

        return new ModelAndView("/pages/theaterPage");
    }
}