package com.OEzoa.OEasy.application.community.DTO.CmnComment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetAllCommentResponseDTO {
    @Schema(name = "유저pk")
    private long memberId;

    @Schema(name = "댓글pk")
    private long commentPk;

    @Schema(name = "내용")
    private String content;

    @Schema(name = "닉네임")
    private String nickname;
}
