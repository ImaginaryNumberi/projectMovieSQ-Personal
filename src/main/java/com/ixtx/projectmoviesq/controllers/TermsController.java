package com.ixtx.projectmoviesq.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class TermsController {

    @RequestMapping(value = "/terms",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getTerms() {
        return new ModelAndView("/pages/terms");
    }

    @RequestMapping(value = "/privacyPolicy",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getPrivacyPolicy() {
        return new ModelAndView("/pages/privacyPolicy");
    }
}
