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
public class AioeRequestDTO {
    @Schema(description = "사용자의 질문", example = "오이가 뭐야?")
    private String question;
}
