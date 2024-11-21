package com.OEzoa.OEasy.application.vote.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class MessageRequest {
    private long userPk;
    private String content;
}
