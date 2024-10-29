package com.OEzoa.OEasy.application.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KakaoResponseDTO<T> {
    private String message;
    private T data;
}
