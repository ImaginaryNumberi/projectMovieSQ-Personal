package com.ixtx.projectmoviesq.services;

import com.ixtx.projectmoviesq.entities.RecoverCodeEntity;
import com.ixtx.projectmoviesq.entities.RecoverPasswordCodeEntity;
import com.ixtx.projectmoviesq.entities.ContactCodeEntity;
import com.ixtx.projectmoviesq.entities.UserEntity;
import com.ixtx.projectmoviesq.enums.*;
import com.ixtx.projectmoviesq.enums.regexp.RegExp;
import com.ixtx.projectmoviesq.mappers.UserMapper;
import com.ixtx.projectmoviesq.utils.CryptoUtil;
import com.ixtx.projectmoviesq.utils.NCloudUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final UserMapper userMapper;

    @Autowired
    public UserService(JavaMailSender mailSender,
                       SpringTemplateEngine templateEngine,
                       UserMapper userMapper) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.userMapper = userMapper;
    }

    // 연락처로 회원 정보 가져오기
    public UserEntity getUserByContact(UserEntity user) {
        return this.userMapper.selectUserByContact(user.getContact());
    }


    // 회원가입 휴대폰 인증번호 보내기
    @Transactional
    public SendRegisterCodeResult sendRegisterCode(ContactCodeEntity contactCode) {
        // 값이 없거나 정규식이 틀린 경우
        if (contactCode == null
                || contactCode.getContact() == null
                || !RegExp.CONTACT.matches(contactCode.getContact())) {
            return SendRegisterCodeResult.FAILURE;
        }

        // 이미 가입된 번호가 있는 경우
        if (this.userMapper.selectUserByContact(contactCode.getContact()) != null) {
            return SendRegisterCodeResult.FAILURE_DUPLICATE;
        }

        String code = RandomStringUtils.randomNumeric(6); // 6자리의 임의의 숫자 문자열을 생성하는 기능
        String salt = CryptoUtil.hashSha512(String.format("%s%s%f%f",
                contactCode.getContact(),
                code,
                Math.random(),
                Math.random())).trim();

        contactCode.setCode(code)
                .setSalt(salt)
                .setCreatedAt(new Date())
                .setExpiresAt(DateUtils.addMinutes(contactCode.getCreatedAt(), 5))
                .setExpired(false);

        NCloudUtil.sendSms(contactCode.getContact(), String.format("[Moviesq] 인증번호\n[%s]를 입력해 주세요.", contactCode.getCode()));

        return this.userMapper.insertRegisterContactCode(contactCode) > 0
                ? SendRegisterCodeResult.SUCCESS
                : SendRegisterCodeResult.FAILURE;
    }


    //  휴대폰 인증번호 확인 (회원가입, 연락처 변경)
    @Transactional
    public VerifyResult verifyCode(ContactCodeEntity contactCode) {
        if (contactCode == null
                || contactCode.getContact() == null
                || contactCode.getSalt() == null
                || contactCode.getCode() == null
                || !RegExp.CONTACT.matches(contactCode.getContact())
                || !RegExp.CODE.matches(contactCode.getCode())
                || !RegExp.SALT.matches(contactCode.getSalt())) {
            return VerifyResult.FAILURE;
        }
        System.out.println("새로운연락처 :" + (contactCode.getContact() != null));
        System.out.println("솔트:" + (contactCode.getSalt() != null));
        System.out.println("코드:" + (contactCode.getCode() != null));
        // DB 인증번호인지 확인
        ContactCodeEntity existingCode = this.userMapper.selectCodeByContactCodeSalt(contactCode.getContact(),
                contactCode.getCode(),
                contactCode.getSalt());
        System.out.println("인증 존재 유무 :" + (existingCode != null));
        // 인증번호 발송한 회원이 아닌 경우
        if (existingCode == null) {
            return VerifyResult.FAILURE;
        }
        // 유효시간이 지난경우
        Date current = new Date();
        if (current.compareTo(existingCode.getExpiresAt()) > 0) {
            return VerifyResult.FAILURE_EXPIRED;
        }
        // 인증완료 데이터 바꿔주기
        existingCode.setExpired(true);
        return this.userMapper.updateCodeExpired(existingCode) > 0
                ? VerifyResult.SUCCESS
                : VerifyResult.FAILURE;
    }

    // 회원가입 인증번호 완료 확인
    public ContactCodeEntity checkRegisterCodeByExpired(@Param(value = "contact") String contact) {
        return this.userMapper.selectRegisterByContact(contact);
    }

    // 회원가입 완료
    @Transactional
    public RegisterResult registerUser(UserEntity user, @Param(value = "birthStr") String birthStr) throws ParseException {
        System.out.println("=====================================");
        System.out.println("회원가입 확인");
        System.out.println("회원 존재 :" + (user != null));
        System.out.println("이메일 존재" + (user.getEmail() != null));
        System.out.println("비번 :" + (user.getPassword() != null));
        System.out.println("생일 :" + (birthStr != null));
        System.out.println("연락처 :" + (user.getContact() != null));
        System.out.println("이메일 정규식 :" + RegExp.EMAIL.matches(user.getEmail()));
        System.out.println("이름 정규식 :" + RegExp.NAME.matches(user.getName()));
        System.out.println("비번 정규식 :" + RegExp.PASSWORD.matches(user.getPassword()));
        System.out.println("생일 정규식 :" + RegExp.BIRTHDAY.matches(birthStr));
        System.out.println("연락처 정규식 :" + RegExp.CONTACT.matches(user.getContact()));
        if (user == null
                || user.getEmail() == null
                || user.getName() == null
                || user.getPassword() == null
                || user.getContact() == null
                || birthStr == null
                || !RegExp.EMAIL.matches(user.getEmail())
                || !RegExp.NAME.matches(user.getName())
                || !RegExp.PASSWORD.matches(user.getPassword())
                || !RegExp.BIRTHDAY.matches(birthStr)
                || !RegExp.CONTACT.matches(user.getContact())) {
            return RegisterResult.FAILURE;
        }
        if (this.userMapper.selectUserByEmail(user.getEmail()) != null) {
            return RegisterResult.FAILURE_EMAIL_DUPLICATE;
        }
        // birth type : String -> Date 바꾸기
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birth = sdf.parse(birthStr);
        user.setBirthday(birth)
                .setPassword(CryptoUtil.hashSha512(user.getPassword()))
                .setStatus("OKAY")
                .setAdmin(false)
                .setRegisteredAt(new Date())
                .setKakaoLinked(false);
        return this.userMapper.insertUser(user) > 0
                && this.userMapper.selectRegisterByContact(user.getContact()).isExpired()
                ? RegisterResult.SUCCESS
                : RegisterResult.FAILURE;
    }

    // 로그인
    @Transactional
    public LoginResult login(UserEntity user) {
        if (user == null
                || user.getEmail() == null
                || !RegExp.EMAIL.matches(user.getEmail())) {
            return LoginResult.FAILURE;
        }
        UserEntity existingUser = this.userMapper.selectUserByEmail(user.getEmail());
        // 가입된 이메일이 없을 때
        if (existingUser == null) {
            return LoginResult.FAILURE_WRONG_ID;
        }
        // 입력받은 비밀번호 암호화
        user.setPassword(CryptoUtil.hashSha512(user.getPassword()));
        // 가입된 이메일의 비밀번호와 입력받은 비밀번호 동일 여부
        if (!user.getPassword().equals(existingUser.getPassword())) {
            return LoginResult.FAILURE_WRONG_PWD;
        }
        // 삭제된 계정
        if (existingUser.getStatus().equals("DELETED")) {
            return LoginResult.FAILURE_DELETED;
        }
        // 사용 중지된 계정
        if (existingUser.getStatus().equals("SUSPENDED")) {
            return LoginResult.FAILURE_SUSPENDED;
        }
        user.setName(existingUser.getName())
                .setBirthday(existingUser.getBirthday())
                .setContact(existingUser.getContact())
                .setAdmin(existingUser.isAdmin())
                .setRegisteredAt(existingUser.getRegisteredAt())
                .setKakaoEmail(existingUser.getKakaoEmail())
                .setKakaoLinked(existingUser.isKakaoLinked());
        return LoginResult.SUCCESS;
    }

    // 아이디 찾기 휴대폰 인증번호 보내기
    @Transactional
    public SendRecoverIdCodeResult sendRecoverIdCode(RecoverCodeEntity recoverCode) {
        if (recoverCode == null
                || recoverCode.getContact() == null
                || !RegExp.CONTACT.matches(recoverCode.getContact())) {
            return SendRecoverIdCodeResult.FAILURE;
        }
        // 가입된 번호가 아닌 경우
        if (this.userMapper.selectUserByContact(recoverCode.getContact()) == null) {
            return SendRecoverIdCodeResult.FAILURE_NOT_USER;
        }
        String code = RandomStringUtils.randomNumeric(6);
        String salt = CryptoUtil.hashSha512(String.format("%s%s%f%f",
                recoverCode.getContact(),
                code,
                Math.random(),
                Math.random()));
        recoverCode.setCreatedAt(new Date());
        recoverCode.setCode(code)
                .setSalt(salt)
                .setCreatedAt(new Date())
                .setExpiresAt(DateUtils.addMinutes(recoverCode.getCreatedAt(), 5))
                .setExpired(false);
        NCloudUtil.sendSms(recoverCode.getContact(), String.format("[Moviesq]아이디 찾기 인증번호 입니다. %s", recoverCode.getCode()));
        return this.userMapper.insertRecoverContactCode(recoverCode) > 0
                ? SendRecoverIdCodeResult.SUCCESS
                : SendRecoverIdCodeResult.FAILURE;
    }

    // 아이디 찾기 휴대폰 인증번호 확인
    @Transactional
    public VerifyResult recoverCodeResult(RecoverCodeEntity recoverCode) {
        if (recoverCode == null
                || recoverCode.getContact() == null
                || recoverCode.getCode() == null
                || recoverCode.getSalt() == null
                || !RegExp.CONTACT.matches(recoverCode.getContact())
                || !RegExp.CODE.matches(recoverCode.getCode())
                || !RegExp.SALT.matches(recoverCode.getSalt())) {
            return VerifyResult.FAILURE;
        }

        // 입력받은 연락처 코드 솔트에 대한 데이터가 있는지
        RecoverCodeEntity existingRecover = this.userMapper.selectRecoverIdByContactCodeSalt(recoverCode.getContact(), recoverCode.getCode(), recoverCode.getSalt());

        // 없다면
        if (existingRecover == null) {
            return VerifyResult.FAILURE;
        }
        // 유효기간 만료여부
        Date current = new Date();
        if (current.compareTo(existingRecover.getExpiresAt()) > 0) {
            return VerifyResult.FAILURE_EXPIRED;
        }

        // 변경여부 변경
        existingRecover.setExpired(true);
        return this.userMapper.updateRecoverExpired(existingRecover) > 0
                ? VerifyResult.SUCCESS
                : VerifyResult.FAILURE;
    }

    // 아이디 찾기
    @Transactional
    public RecoverIdResult findId(UserEntity user,
                                  @Param(value = "birthStr") String birthStr,
                                  RecoverCodeEntity recoverCode) throws ParseException {
        if (user == null
                || birthStr == null
                || recoverCode == null
                || !RegExp.NAME.matches(user.getName())
                || !RegExp.BIRTHDAY.matches(birthStr)
                || !RegExp.CONTACT.matches(recoverCode.getContact())
                || !RegExp.CODE.matches(recoverCode.getCode())
                || !RegExp.SALT.matches(recoverCode.getSalt())) {
            return RecoverIdResult.FAILURE;
        }

        // 인증번호 확인 완료 여부
        RecoverCodeEntity existingRecoverCode = this.userMapper.selectRecoverIdByContactCodeSalt(recoverCode.getContact(), recoverCode.getCode(), recoverCode.getSalt());

        // 인증번호 확인 하지 않은 경우
        if (existingRecoverCode == null || !existingRecoverCode.isExpired()) {
            return RecoverIdResult.FAILURE_NOT_VERIFY;
        }

        // birth type : String -> Date 바꾸기
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birth = sdf.parse(birthStr);

        // 가입한 회원 정보 불러오기
        UserEntity existingUser = this.userMapper.selectUserByNameContact(user.getName(), user.getContact());

        return existingUser != null && birth.equals(existingUser.getBirthday())
                ? RecoverIdResult.SUCCESS
                : RecoverIdResult.FAILURE;
    }

    // 비밀번호 재설정 이메일코드 보내기
    public SendRecoverPwdCodeResult sendRecoverPasswordCode(RecoverPasswordCodeEntity recoverPasswordCode, UserEntity user) throws MessagingException, IOException {
        if (recoverPasswordCode == null
                || recoverPasswordCode.getEmail() == null
                || user.getName() == null
                || !RegExp.EMAIL.matches(recoverPasswordCode.getEmail())
                || !RegExp.NAME.matches(user.getName())) {
            return SendRecoverPwdCodeResult.FAILURE;
        }

        if (this.userMapper.selectUserByEmailName(recoverPasswordCode.getEmail(), user.getName()) == null) {
            return SendRecoverPwdCodeResult.FAILURE_NOT_USER;
        }

        recoverPasswordCode
                .setCode(RandomStringUtils.randomNumeric(6))
                .setSalt(CryptoUtil.hashSha512(String.format("%s%s%f%f",
                        recoverPasswordCode.getCode(),
                        recoverPasswordCode.getEmail(),
                        Math.random(),
                        Math.random())).trim())
                .setCreatedAt(new Date())
                .setexpiresAt(DateUtils.addMinutes(recoverPasswordCode.getCreatedAt(), 10))
                .setExpired(false);

        String url = String.format("http://moviesq.herrykim.com/recoverPassword?email=%s&code=%s&salt=%s",
                recoverPasswordCode.getEmail(),
                recoverPasswordCode.getCode(),
                recoverPasswordCode.getSalt());

        Context context = new Context();
        context.setVariable("url", url);

        //  JavaMail API를 사용하여 이메일을 보내기
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // 이메일 제목
        helper.setSubject("[Moviesq] 비밀번호 재설정을 위한 이메일 인증");

        // 보내는 사람
        helper.setFrom("kimh3418@gmail.com", "Movie SQ");

        // 받는 사람
        helper.setTo(recoverPasswordCode.getEmail());

        // 이메일 본문 설정 (HTML 보내기)
        helper.setText(templateEngine.process("recoverPwd_check", context), true);

        this.mailSender.send(mimeMessage);
        return this.userMapper.insertRecoverPwdCode(recoverPasswordCode) > 0
                ? SendRecoverPwdCodeResult.SUCCESS
                : SendRecoverPwdCodeResult.FAILURE;
    }

    // 비밀번호 재설정 : 유효한지 확인
    @Transactional
    public VerifyResult verifyRecoverPasswordCode(RecoverPasswordCodeEntity recoverPasswordCode) {
        System.out.println("=====================================");
        System.out.println("이메일 확인");
        System.out.println("코드 존재 :" + (recoverPasswordCode != null));
        System.out.println("이멜 존재" + (recoverPasswordCode.getEmail() != null));
        System.out.println("코드 :" + (recoverPasswordCode.getCode() != null));
        System.out.println("솔트:" + (recoverPasswordCode.getSalt() != null));
        System.out.println(recoverPasswordCode.getCode());
        System.out.println(recoverPasswordCode.getSalt());
        System.out.println(recoverPasswordCode.getSalt().length());
        System.out.println("이메일 정규식 :" + RegExp.EMAIL.matches(recoverPasswordCode.getEmail()));
        System.out.println("코드 정규식 :" + RegExp.CODE.matches(recoverPasswordCode.getCode()));
        System.out.println("솔트 정규식 :" + RegExp.SALT.matches(recoverPasswordCode.getSalt()));

        if (recoverPasswordCode == null
                || recoverPasswordCode.getEmail() == null
                || recoverPasswordCode.getCode() == null
                || recoverPasswordCode.getSalt() == null
                || !RegExp.EMAIL.matches(recoverPasswordCode.getEmail())
                || !RegExp.CODE.matches(recoverPasswordCode.getCode())
                || !RegExp.SALT.matches(recoverPasswordCode.getSalt())) {
            return VerifyResult.FAILURE;
        }

        recoverPasswordCode = this.userMapper.selectRecoverPwdCodeByEmailCodeSalt(recoverPasswordCode);
        System.out.println("존재하는 코드 :" + (recoverPasswordCode != null));
        if (recoverPasswordCode == null) {
            return VerifyResult.FAILURE;
        }

        if (new Date().compareTo(recoverPasswordCode.getexpiresAt()) > 0) {
            return VerifyResult.FAILURE_EXPIRED;
        }

        recoverPasswordCode.setExpired(true);

        System.out.println("업데이트: " + (this.userMapper.updateRecoverPwdCode(recoverPasswordCode)));
        return this.userMapper.updateRecoverPwdCode(recoverPasswordCode) > 0
                ? VerifyResult.SUCCESS
                : VerifyResult.FAILURE;
    }

    // 비밀번호 재설정하기
    @Transactional
    public CommonResult modifyPassword(RecoverPasswordCodeEntity recoverPasswordCode, UserEntity user) {
        System.out.println("=====================================");
        System.out.println("비번 변경");
        System.out.println("코드존재 :" + recoverPasswordCode != null);
        System.out.println("패스워드 존재" + user.getPassword());
        System.out.println("이메일 :" + recoverPasswordCode.getEmail() != null);
        System.out.println("코드 :" + recoverPasswordCode.getCode() != null);
        System.out.println("솔트 :" + recoverPasswordCode.getSalt() != null);
        System.out.println("비번 정규식:" + RegExp.PASSWORD.matches(user.getPassword()));
        System.out.println("이메일 정규식 :" + RegExp.EMAIL.matches(recoverPasswordCode.getEmail()));
        System.out.println("코드 정규식 :" + RegExp.CODE.matches(recoverPasswordCode.getCode()));
        System.out.println("솔트 정규식 :" + RegExp.SALT.matches(recoverPasswordCode.getSalt()));

        if (recoverPasswordCode == null
                || user.getPassword() == null
                || recoverPasswordCode.getEmail() == null
                || recoverPasswordCode.getCode() == null
                || recoverPasswordCode.getSalt() == null
                || !RegExp.PASSWORD.matches(user.getPassword())
                || !RegExp.EMAIL.matches(recoverPasswordCode.getEmail())
                || !RegExp.CODE.matches(recoverPasswordCode.getCode())
                || !RegExp.SALT.matches(recoverPasswordCode.getSalt())) {
            return CommonResult.FAILURE;
        }

        RecoverPasswordCodeEntity existingRecoverCode = this.userMapper.selectRecoverPwdCodeByEmailCodeSalt(recoverPasswordCode);
        if (existingRecoverCode == null) {
            return CommonResult.FAILURE;
        }
        UserEntity existingUser = this.userMapper.selectUserByEmail(existingRecoverCode.getEmail());
        System.out.println("회원 존재유무 :" + (existingUser != null));
        if (existingUser == null) {
            return CommonResult.FAILURE;
        }
        existingUser.setPassword(CryptoUtil.hashSha512(user.getPassword()));
        return (this.userMapper.updateUserPassword(existingUser) > 0) && (this.userMapper.deleteRecoverPasswordCode(existingRecoverCode) > 0)
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;

    }

    public UserEntity findUser(@Param("kakaoEmail") String kakaoEmail) {
        return this.userMapper.selectUserByKakaoEmail(kakaoEmail);
    }


}