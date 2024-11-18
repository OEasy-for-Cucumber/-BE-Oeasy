package com.OEzoa.OEasy.application.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NicknameResponseDTO {
    @Schema(description = "변경할 닉네임", example = "변경변경")
    private String nickname;
    @Schema(description = "응답 메시지", example = "닉네임 변경 성공")
    private String message;
}
