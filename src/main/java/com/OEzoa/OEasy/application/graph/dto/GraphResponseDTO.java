package com.OEzoa.OEasy.application.graph.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphResponseDTO {
    @Schema(description = "날짜", example = "2024-10-31")
    private String date;
    @Schema(description = "가격", example = "1835")
    private Long price;
}
