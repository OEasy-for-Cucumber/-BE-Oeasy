package com.OEzoa.OEasy.application.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KakaoDTO {

    @Schema(description = "카카오 ID", example = "3770715377")
    private long id;
    @Schema(description = "아이디", example = "abc123@naver.com")
    private String email;
    @Schema(description = "닉네임", example = "김현빈")
    private String nickname;
}