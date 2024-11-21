package com.OEzoa.OEasy.application.vote.DTO;

import lombok.Getter;

@Getter
public class MessageResponse {
    private String content;

    public MessageResponse(String content) {
        this.content = content;
    }
}
