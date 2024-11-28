package com.OEzoa.OEasy.application.graph.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphRequestDTO {
    @Schema(description = "검색 시작 날짜", example = "2024-10-31")
    private String startDate;
    @Schema(description = "검색 끝나는 날짜", example = "2024-11-11")
    private String endDate;
}
