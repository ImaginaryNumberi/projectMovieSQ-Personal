package com.ixtx.projectmoviesq.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ixtx.projectmoviesq.dtos.KakaoDto;
import com.ixtx.projectmoviesq.entities.UserEntity;
import com.ixtx.projectmoviesq.mappers.UserMapper;
import com.ixtx.projectmoviesq.dtos.OAuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoService {

    private final UserMapper userMapper;
    private static final String GRANT_TYPE = "authorization_code";
    private static final String CLIENT_ID = "4882aeb707f4054de864814b0c14ce58";
    private static final String REDIRECT_URL = "https://moviesq.herrykim.com";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String userInfo_URL = "https://kapi.kakao.com/v2/user/me";

    @Autowired
    public KakaoService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public String getAccessToken(String code)  {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", CONTENT_TYPE);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", GRANT_TYPE);
        params.add("client_id", CLIENT_ID);
        params.add("redirect-uri", REDIRECT_URL);
        params.add("code", code);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                TOKEN_URL,
                HttpMethod.POST,
                httpEntity,
                String.class
        );
        System.out.println(response);
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return oauthToken.getAccess_token();
    }

    public KakaoDto getUserInfo(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + accessToken);
        headers.add("Content-type", CONTENT_TYPE);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                userInfo_URL,
                HttpMethod.POST,
                httpEntity,
                String.class
        );
        System.out.println(response);
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoDto kakaoDto = null;
        try{
            kakaoDto = objectMapper.readValue(response.getBody(), KakaoDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return kakaoDto;
    }

    public int putKakaoEmail(UserEntity user){
        return this.userMapper.updatekakaoLinkedState(user);
    }

}
