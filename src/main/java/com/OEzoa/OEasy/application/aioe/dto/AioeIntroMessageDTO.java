package com.OEzoa.OEasy.application.aioe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "챗봇 시작 응답 DTO")
public class AioeIntroMessageDTO {
    @Schema(description = "초기 메시지", example = "안녕하세오이! 저는 AI 오이입니다오이!")
    private String initialMessage;

    @Schema(description = "챗봇 시작 시간", example = "2024-11-21 00:28:31")
    private String startTime;
}
