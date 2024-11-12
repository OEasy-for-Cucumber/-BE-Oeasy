package com.OEzoa.OEasy.application.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpDTO {
    @Schema(description = "회원 이메일", example = "example@domain.com")
    @NotEmpty(message = "이메일은 필수 항목입니다.")
    private String email;
    @Schema(description = "회원 비밀번호", example = "password1234")
    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    private String pw;
    @Schema(description = "회원 닉네임", example = "Hyunbin Kim")
    @NotEmpty(message = "닉네임은 필수 항목입니다.")
    private String nickname;
    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.png")
    private String memberImage;
}