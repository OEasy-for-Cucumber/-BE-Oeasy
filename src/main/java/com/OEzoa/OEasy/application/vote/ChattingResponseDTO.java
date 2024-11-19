package com.OEzoa.OEasy.application.vote;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChattingResponseDTO {
    @Schema(description = "유저pk")
    private long id;
    @Schema(description = "메시지 내용")
    private String content;
    @Schema(description = "프로필 이미지 주소")
    private String profileImg;
    @Schema(description = "닉네임")
    private String nickname;



}
