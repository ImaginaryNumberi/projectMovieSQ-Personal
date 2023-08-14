package com.ixtx.projectmoviesq.controllers;

import com.ixtx.projectmoviesq.dtos.KakaoDto;
import com.ixtx.projectmoviesq.entities.UserEntity;
import com.ixtx.projectmoviesq.enums.CommonResult;
import com.ixtx.projectmoviesq.services.KakaoService;
import com.ixtx.projectmoviesq.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.SessionException;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping(value = "")
public class KakaoController {

    private final KakaoService kakaoService;
    private final UserService userService;

    @Autowired
    public KakaoController(KakaoService kakaoService, UserService userService) {
        this.kakaoService = kakaoService;
        this.userService = userService;
    }

    @RequestMapping(value = "auth/kakao/callback", method = RequestMethod.GET)
    public synchronized ModelAndView kakaoCallBack(@RequestParam(value = "code") String code,
                                      @SessionAttribute(value = "user", required = false) UserEntity user,
                                      HttpServletRequest request) throws IOException {

        // 인증코드로 토큰 발급 받기
        String accessToken = this.kakaoService.getAccessToken(code);
        // 토큰으로 사용자 정보 받기
        KakaoDto kakaoUser = this.kakaoService.getUserInfo(accessToken);

        // 카카오 이메일
        String kakaoEmail = kakaoUser.getKakao_account().getEmail();

        // 카카오 이메일 등록된 회원 찾기
        UserEntity existingUser = this.userService.findUser(kakaoEmail);
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();

        // 카카오 계정 첫 연동 (로그인 상태)
        if (existingUser == null && user != null) {
            UserEntity userEntity = this.userService.getUserByContact(user);
            userEntity.setKakaoEmail(kakaoEmail);
            userEntity.setKakaoLinked(true);
            if (this.kakaoService.putKakaoEmail(userEntity) > 0) {
                modelAndView.addObject("link", "success");
                modelAndView.setViewName("redirect:userCheck");
                return modelAndView;
            }
            // 첫 로그인이 아닐 때, 로그인 연결 상태 일때
        } else if (existingUser != null && existingUser.isKakaoLinked() && user != null) {
            existingUser.setKakaoLinked(false);
            this.kakaoService.putKakaoEmail(existingUser);
            modelAndView.addObject("disconnected", "success");
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        } else if (existingUser != null && !existingUser.isKakaoLinked() && user != null) {
            existingUser.setKakaoLinked(true);
            this.kakaoService.putKakaoEmail(existingUser);
            session.setAttribute("user", existingUser);
            modelAndView.addObject("connected", "success");
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        } else if (existingUser != null && user == null && !existingUser.isKakaoLinked() ) {
            modelAndView.addObject("notConnect", "success");
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        } else {
            session.setAttribute("user", existingUser);
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }


}
