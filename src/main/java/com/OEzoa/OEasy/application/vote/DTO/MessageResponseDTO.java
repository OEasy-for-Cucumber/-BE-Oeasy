package com.OEzoa.OEasy.application.vote.DTO;

import lombok.Getter;

@Getter
public class MessageResponseDTO {
    private String content;

    public MessageResponseDTO(String content) {
        this.content = content;
    }
}
