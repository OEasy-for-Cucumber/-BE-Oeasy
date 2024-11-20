package com.OEzoa.OEasy.api.vote;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class MessageRequest {
    private long userPk;
    private String content;
}
