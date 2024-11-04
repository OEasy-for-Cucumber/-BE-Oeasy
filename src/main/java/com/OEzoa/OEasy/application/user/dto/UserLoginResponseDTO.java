package com.OEzoa.OEasy.application.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserLoginResponseDTO {
    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNiIsImlhdCI6MTczMDY4NTE5OCwiZXhwIjoxNzMwNjg4Nzk4fQ.Rq95AiUnHzqlPkEqLYQvlxWCon_3CUTrquqdmd1T-k0")
    private String accessToken;
    @Schema(description = "아이디", example = "user123@naver.com")
    private String email;
    @Schema(description = "닉네임", example = "user123@naver.com")
    private String nickname;
}
