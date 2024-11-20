package com.OEzoa.OEasy.application.aioe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistoryDTO {
    @Schema(description = "대화 내용 목록")
    private List<ChatMessageDTO> messages;
}
