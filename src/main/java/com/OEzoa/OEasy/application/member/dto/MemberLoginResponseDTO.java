package com.OEzoa.OEasy.application.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.service.annotation.GetExchange;

@Data
@Builder
@Getter
@AllArgsConstructor
public class MemberLoginResponseDTO {
    @Schema(description = "JWT 액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;
    @Schema(description = "회원 이메일", example = "example@domain.com")
    private String email;
    @Schema(description = "회원 닉네임", example = "Hyunbin Kim")
    private String nickname;
}
