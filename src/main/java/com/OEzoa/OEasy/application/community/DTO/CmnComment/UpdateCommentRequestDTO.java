package com.OEzoa.OEasy.application.community.DTO.CmnComment;

import lombok.Getter;

@Getter
public class UpdateCommentRequestDTO {
    private long commentId;
    private long memberId;
    private String content;
}
