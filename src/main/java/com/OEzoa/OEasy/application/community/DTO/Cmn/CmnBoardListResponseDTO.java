package com.OEzoa.OEasy.application.community.DTO.Cmn;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CmnBoardListResponseDTO {

    @Schema(description = "보드 pk")
    private Long boardPk;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "조회수")
    private Integer viewCnt;

    @Schema(description = "좋아요")
    private Integer likeCnt;

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "썸네일 이미지 URL")
    private String thumbnailUrl;
}
