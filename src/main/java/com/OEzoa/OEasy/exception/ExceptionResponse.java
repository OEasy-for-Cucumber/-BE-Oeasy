package com.OEzoa.OEasy.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Builder
public class ExceptionResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;

    public static ResponseEntity<ExceptionResponse> toResponse(GlobalExceptionCode code) {
        log.info("예외 발생! httpStatus : {} | message : {} | description : {}", code.getHttpStatus(), code.getMessage(), code.getDescription());
        return ResponseEntity.status(code.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .message(code.getMessage())
                        .status(code.getHttpStatus().value())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
