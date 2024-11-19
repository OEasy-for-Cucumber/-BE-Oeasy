package com.OEzoa.OEasy.application.member.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PasswordChangeRequestDTO {
    @Schema(description = "기존 비밀번호", example = "!a12341234", required = true)
    private String oldPw;
    @Schema(description = "변경할 비밀번호", example = "@b98769876", required = true)
    private String newPw;
}
