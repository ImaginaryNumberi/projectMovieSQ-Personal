package com.ixtx.projectmoviesq.mappers;

import com.ixtx.projectmoviesq.entities.RecoverCodeEntity;
import com.ixtx.projectmoviesq.entities.RecoverPasswordCodeEntity;
import com.ixtx.projectmoviesq.entities.ContactCodeEntity;
import com.ixtx.projectmoviesq.entities.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface UserMapper {
    // 인증번호 전송 (회원가입)
    int insertRegisterContactCode(ContactCodeEntity contactCode);

    // 회원가입
    int insertUser(UserEntity user);

    // 인증번호 전송 (아이디 찾기)
    int insertRecoverContactCode(RecoverCodeEntity recoverCode);

    // 인증번호 이메일 전송 (비밀번호 재설정)
    int insertRecoverPwdCode(RecoverPasswordCodeEntity recoverPasswordCode);

    // 인증번호 전송(회원가입, 연락처 중복확인)
    UserEntity selectUserByContact(@Param(value = "contact") String contact);

    // 인증번호 확인 (회원가입)
    ContactCodeEntity selectCodeByContactCodeSalt(@Param(value = "contact") String contact,
                                                  @Param(value = "code") String code,
                                                  @Param(value = "salt") String salt);

    // 회원가입 휴대폰 인증 여부
    ContactCodeEntity selectRegisterByContact(@Param(value = "contact") String contact);

    // 회원가입 이메일 중복, 로그인 , 회원여부(비밀번호 재설정)
    UserEntity selectUserByEmail(@Param("email") String email);

    // 카카오 간편 로그인 연동
    UserEntity selectUserByKakaoEmail(@Param("kakaoEmail") String kakaoEmail);

    // 아이디 찾기
    UserEntity selectUserByNameContact(@Param(value = "name") String name, @Param(value = "contact") String contact);

    // 인증번호 확인 (아이디 찾기)
    RecoverCodeEntity selectRecoverIdByContactCodeSalt(@Param(value = "contact") String contact, @Param(value = "code") String code, @Param(value = "salt") String salt);

    // 회원 여부 (비밀번호 재설정)
    UserEntity selectUserByEmailName(@Param(value = "email") String email,
                                     @Param(value = "name") String name);

    // 회원가입 인증번호 확인후 상태 변경
    int updateCodeExpired(ContactCodeEntity contactCode);

    // 아이디 찾기 인증번호 확인후 상태 변경
    int updateRecoverExpired(RecoverCodeEntity recoverCode);

    // 회원 확인 (비밀번호 재설정)
    RecoverPasswordCodeEntity selectRecoverPwdCodeByEmailCodeSalt(RecoverPasswordCodeEntity recoverPasswordCode);

    // 비밀번호 재설정 인증 상태 변경
    int updateRecoverPwdCode(RecoverPasswordCodeEntity recoverPasswordCode);

    // 비밀번호 재설정
    int updateUserPassword(UserEntity user);

    int updatekakaoLinkedState(UserEntity user);

    // 비밀번호 재설정 완료 후 데이터 삭제
    int deleteRecoverPasswordCode(RecoverPasswordCodeEntity recoverPasswordCode);
}