package com.single.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({MemberException.class})
    public ResponseEntity<ExceptionResponse> customRequestException(final MemberException exception) {
        log.warn("api Exception: {}", exception.getErrorCode());
        return ResponseEntity.badRequest().body(new ExceptionResponse(
                exception.getMessage(), exception.getErrorCode()));
    }

    @Getter
    @AllArgsConstructor
    public static class ExceptionResponse {
        private String message;
        private ErrorCode errorCode;
    }
}
