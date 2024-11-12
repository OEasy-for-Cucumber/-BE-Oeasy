package com.OEzoa.OEasy.application.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginDTO {
    @Schema(description = "회원 이메일", example = "example@domain.com")
    private String email;
    @Schema(description = "회원 비밀번호", example = "password1234")
    private String pw;
}