package com.single.user.exception;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException{
    private final ErrorCode errorCode;

    public MemberException(ErrorCode errorCode) {
        super(errorCode.getContent());
        this.errorCode = errorCode;
    }
}
