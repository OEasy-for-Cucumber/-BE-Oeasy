package com.OEzoa.OEasy.application.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDTO {
    @Schema(description = "회원 Pk", example = "1")
    private Long memberPk;

    @Schema(description = "이메일", example = "abc123@naver.com")
    private String email;

    @Schema(description = "닉네임", example = "김현빈")
    private String nickname;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.png")
    private String memberImage;

    @Schema(description = "카카오 ID", example = "12312321321321")
    private Long kakaoId;
}
