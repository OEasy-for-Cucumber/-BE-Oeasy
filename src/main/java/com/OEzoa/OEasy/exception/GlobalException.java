package com.OEzoa.OEasy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GlobalException extends RuntimeException {
    private GlobalExceptionCode globalExceptionCode;
}
