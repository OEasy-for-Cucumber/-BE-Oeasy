package com.OEzoa.OEasy.application.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenResponseDTO {
    @Schema(description = "액세스 토큰", example = "23123123123123123123123123123214124123312312")
    private String accessToken;
    @Schema(description = "리프레시 토큰", example = "123123123123123112312312312323123123213123123")
    private String refreshToken;
}
