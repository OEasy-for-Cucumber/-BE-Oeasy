package com.OEzoa.OEasy.application.vote.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class MessageRequestDTO {
    private long userPk;
    private String content;
}
