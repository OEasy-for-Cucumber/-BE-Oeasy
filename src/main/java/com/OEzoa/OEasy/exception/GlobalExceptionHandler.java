package com.OEzoa.OEasy.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    protected ResponseEntity<ExceptionResponse> globalException(GlobalException e) {
        return ExceptionResponse.toResponse(e.getGlobalExceptionCode());
    }

}
