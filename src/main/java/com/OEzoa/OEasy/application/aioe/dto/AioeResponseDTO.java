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
public class AioeResponseDTO {
    @Schema(description = "응답 메시지", example = "오이는 과일이 아니야, 오이!")
    private String message;   // GPT 응답
    @Schema(description = "시간", example = "2024-11-20T13:52:00")
    private String timestamp; // 기록된 시간
}
