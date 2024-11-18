package com.OEzoa.OEasy.application.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NicknameRequestDTO {
    @Schema(description = "새로운 닉네임", example = "변경 닉네임", required = true)
    private String newNickname;
}
