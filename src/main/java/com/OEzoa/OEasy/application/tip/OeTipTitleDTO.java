package com.OEzoa.OEasy.application.tip;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OeTipTitleDTO {
    @Schema(description = "내용", example = "천영 물질")
    private String content;
    @Schema(description = "색", example = "white")
    private String color;
    @Schema(description = "순서", example = "0")
    private Integer order;
}
