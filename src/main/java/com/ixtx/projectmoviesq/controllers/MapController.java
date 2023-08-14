package com.ixtx.projectmoviesq.controllers;

import com.ixtx.projectmoviesq.entities.TheaterEntity;
import com.ixtx.projectmoviesq.services.TheaterService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/map")
public class MapController {

    public final TheaterService theaterService;

    public MapController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMovie() {
        return new ModelAndView("/pages/theaterMap");
    }
}
