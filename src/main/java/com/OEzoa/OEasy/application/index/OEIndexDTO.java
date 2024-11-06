package com.OEzoa.OEasy.application.index;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OEIndexDTO {
    @Schema(description = "오이 종류", example = "기쁜 오이")
    private String cucumberType;

    @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
    private String imgUrl;

    @Schema(description = "날씨 상태", example = "맑음")
    private String weatherState;

    @Schema(description = "온도", example = "24.1")
    private double temperature;

    @Schema(description = "측정 시간")
    private LocalDateTime dateTime;
}
