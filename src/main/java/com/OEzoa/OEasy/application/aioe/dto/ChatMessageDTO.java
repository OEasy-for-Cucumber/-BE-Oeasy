package com.OEzoa.OEasy.application.aioe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    @Schema(description = "메시지 내용", example = "오이는 건강에 좋아요!")
    private String message;

    @Schema(description = "메시지 발신자 (user/aioe)", example = "user")
    private String type;

    @Schema(description = "메시지 생성 시간", example = "2024-11-20T19:25:42")
    private String date;
}
