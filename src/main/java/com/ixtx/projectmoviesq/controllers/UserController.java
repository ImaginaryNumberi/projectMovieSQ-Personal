package com.ixtx.projectmoviesq.controllers;

import com.ixtx.projectmoviesq.entities.RecoverCodeEntity;
import com.ixtx.projectmoviesq.entities.RecoverPasswordCodeEntity;
import com.ixtx.projectmoviesq.entities.ContactCodeEntity;
import com.ixtx.projectmoviesq.entities.UserEntity;
import com.ixtx.projectmoviesq.enums.*;
import com.ixtx.projectmoviesq.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;


@Controller
@RequestMapping(value = "/")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 화면 보이기
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public ModelAndView getRegister() {
        return new ModelAndView("home/register");
    }

    // ID 찾기, 비밀번호 재설정
    @RequestMapping(value = "recover", method = RequestMethod.GET)
    public ModelAndView getRecover() {
        return new ModelAndView("home/recover");
    }

    // 회원가입 휴대폰 인증번호 보내기
    @ResponseBody
    @RequestMapping(value = "contactCode",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getContactCode(ContactCodeEntity contactCode) {
        SendRegisterCodeResult result = this.userService.sendRegisterCode(contactCode);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if (result == SendRegisterCodeResult.SUCCESS) {
            responseObject.put("salt", contactCode.getSalt());
        }
        return responseObject.toString();
    }

    // 아이디 찾기 휴대폰 인증번호 보내기
    @ResponseBody
    @RequestMapping(value = "contactCodeRec",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getContactCodeRec(RecoverCodeEntity recoverCode) {
        SendRecoverIdCodeResult result = this.userService.sendRecoverIdCode(recoverCode);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if (result == SendRecoverIdCodeResult.SUCCESS) {
            responseObject.put("salt", recoverCode.getSalt());
        }
        return responseObject.toString();
    }

    // 비밀번호 재설정
    @RequestMapping(value = "recoverPassword", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRecoverPassword(RecoverPasswordCodeEntity recoverPasswordCode) {
        VerifyResult result = this.userService.verifyRecoverPasswordCode(recoverPasswordCode);
        ModelAndView modelAndView = new ModelAndView("home/recoverPassword");
        modelAndView.addObject("result", result.toString().toLowerCase());
        modelAndView.addObject("recoverPasswordCode", recoverPasswordCode);
        return modelAndView;
    }

    // 아이디 찾기
    @ResponseBody
    @RequestMapping(value = "searchId",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String postContactCodeRec(UserEntity user,
                                     @RequestParam(value = "birthStr") String birthStr,
                                     RecoverCodeEntity recoverCode) throws ParseException {
        RecoverIdResult result = this.userService.findId(user, birthStr, recoverCode);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if (result == RecoverIdResult.SUCCESS) {
            user = this.userService.getUserByContact(user);
            responseObject.put("email", user.getEmail());
            responseObject.put("name", user.getName());
        }
        return responseObject.toString();
    }

    // 비밀번호 재설정 : 이메일 인증 전송
    @ResponseBody
    @RequestMapping(value = "recoverPassword",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String postRecoverPassword(RecoverPasswordCodeEntity recoverPasswordCode, UserEntity user) throws MessagingException, IOException {
        SendRecoverPwdCodeResult result = this.userService.sendRecoverPasswordCode(recoverPasswordCode, user);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }

    // 로그인 화면 보이기
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView getLogin(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return new ModelAndView("redirect:./");
        }
        return new ModelAndView("home/login");
    }

    // 로그인
    @ResponseBody
    @RequestMapping(value = "login",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String postLogin(HttpServletRequest request, UserEntity user) {
        LoginResult result = this.userService.login(user);
        HttpSession session = request.getSession();
        if (result == LoginResult.SUCCESS) {
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(1800); // 로그인 유효시간 30분
        }
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        responseObject.put("url", session.getAttribute("url"));
        return responseObject.toString();
    }

    //로그아웃 -> 메인화면으로 돌아가기
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ModelAndView getLogout(HttpSession session) {
        session.removeAttribute("user");
        session.removeAttribute("url");
        return new ModelAndView("redirect:./");
    }

    // 회원가입 진행 완료
    @ResponseBody
    @RequestMapping(value = "register",
            method = RequestMethod.POST)
    public String postRegister(UserEntity user, @RequestParam(value = "birthStr") String birthStr) throws ParseException {
        RegisterResult result = this.userService.registerUser(user, birthStr);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }

    // 회원가입 인증여부 확인
    @ResponseBody
    @RequestMapping(value = "checkContactCode",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public String postCheckContactCode(@RequestParam(value = "contact") String contact) {
        ContactCodeEntity existingRegisterCode = this.userService.checkRegisterCodeByExpired(contact);
        return String.valueOf(existingRegisterCode.isExpired());
    }

    //  휴대폰 인증번호 확인
    @ResponseBody
    @RequestMapping(value = "contactCode",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String patchContactCode(ContactCodeEntity contactCode) {
        VerifyResult result = this.userService.verifyCode(contactCode);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }

    // 아이디 찾기 휴대폰 인증번호 확인
    @ResponseBody
    @RequestMapping(value = "contactCodeRec",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String patchContactCodeRec(RecoverCodeEntity recoverCode) {
        VerifyResult result = this.userService.recoverCodeResult(recoverCode);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }

    // 비밀번호 재설정 완료
    @ResponseBody
    @RequestMapping(value = "recoverPassword", method = RequestMethod.PATCH, produces = MediaType.TEXT_HTML_VALUE)
    public String patchRecoverPassword(RecoverPasswordCodeEntity recoverPasswordCode, UserEntity user) {
        CommonResult result = this.userService.modifyPassword(recoverPasswordCode, user);
        return result.name().toLowerCase();
    }
}