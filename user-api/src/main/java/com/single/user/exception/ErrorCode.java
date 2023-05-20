package com.single.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    ALREADY_REGISTER_MEMBER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자 입니다."),
    ALREADY_VERIFY(HttpStatus.BAD_REQUEST, "이미 인증된 사용자 입니다."),
    WRONG_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증번호 입니다."),
    EXPIRE_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증번호 기간이 만료 되었습니다."),

    LOGIN_MAIL_CHECK_FAIL(HttpStatus.BAD_REQUEST, "아이디를 확인해 주세요."),
    LOGIN_PASSWORD_CECK_FAIL(HttpStatus.BAD_REQUEST, "패스워드를 확인해 주세요."),
    NOT_VERIFIED_EMAIL_CODE(HttpStatus.BAD_REQUEST, "인증되지 않은 메일입니다. 사용자 인증을 위해 메일을 확인해 주세요.");

    private final HttpStatus httpStatus;
    private final String content;
}
