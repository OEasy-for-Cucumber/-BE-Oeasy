package com.OEzoa.OEasy.application.community.DTO.Cmn;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CmnBoardListRequestDTO {
    @Schema(description = "페이지")
    private int page;

    @Schema(description = "한 번에 보여줄 양")
    private int size;

    @Schema(description = "검색어")
    private String searchKeyword;

    @Schema(description = "검색 타입", example = "1. titleAndContent 2. title\n3. nickname")
    private String searchType;

    @Schema(description = "정렬할 키워드", example = "1. 좋아요 : likeCnt\n2. 시간 : boardPk")
    private String sortKeyword;

    @Schema(description = "정렬 타입", example = "true : 오름차\nfalse : 내림차")
    private boolean sortType;


}
