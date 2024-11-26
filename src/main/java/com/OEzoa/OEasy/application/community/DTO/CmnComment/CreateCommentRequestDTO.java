package com.OEzoa.OEasy.application.community.DTO.CmnComment;

import lombok.Getter;

@Getter
public class CreateCommentRequestDTO {
    private long communityId;
    private long memberId;
    private String content;
    private int size;

}
