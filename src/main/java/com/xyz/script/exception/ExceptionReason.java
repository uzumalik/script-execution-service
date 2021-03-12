package com.xyz.script.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionReason {

    COMPILATION_ERROR(HttpStatus.BAD_REQUEST),
    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(HttpStatus.BAD_REQUEST);

    private HttpStatus httpStatus;
}
