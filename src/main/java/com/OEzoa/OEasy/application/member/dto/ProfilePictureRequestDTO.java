package com.OEzoa.OEasy.application.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePictureRequestDTO {
    @Schema(description = "사용자 닉네임", example = "샛별넴", required = true)
    private String nickname;

    @Schema(description = "업로드할 이미지의 URL", example = "https://example.com/image.png", required = true)
    private String imageUrl;
}
