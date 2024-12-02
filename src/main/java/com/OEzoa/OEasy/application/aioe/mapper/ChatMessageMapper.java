package com.OEzoa.OEasy.application.aioe.mapper;

import com.OEzoa.OEasy.application.aioe.dto.AioeIntroMessageDTO;
import com.OEzoa.OEasy.application.aioe.dto.AioeResponseDTO;
import com.OEzoa.OEasy.application.aioe.dto.ChatHistoryDTO;
import com.OEzoa.OEasy.application.aioe.dto.ChatMessageDTO;
import com.OEzoa.OEasy.domain.aioe.AiOe;
import com.OEzoa.OEasy.domain.aioe.AiOeChatMessage;
import com.OEzoa.OEasy.domain.member.Member;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ChatMessageMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static AioeIntroMessageDTO toStartResponseDto(AiOeChatMessage initialMessage) {
        return AioeIntroMessageDTO.builder()
                .initialMessage(initialMessage.getMessage())
                .startTime(initialMessage.getDateTime().format(FORMATTER))
                .build();
    }

    public static AiOeChatMessage toEntity(String message, String type, AiOe aiOe) {
        return AiOeChatMessage.builder()
                .message(message)
                .type(type)
                .aiOe(aiOe)
                .dateTime(LocalDateTime.now())
                .build();
    }

    public static ChatMessageDTO toDto(AiOeChatMessage aiOeChatMessage) {
        return ChatMessageDTO.builder()
                .message(aiOeChatMessage.getMessage())
                .type(aiOeChatMessage.getType())
                .date(aiOeChatMessage.getDateTime().format(FORMATTER))
                .build();
    }

    // 응답 DB에 넣을때 시간 포맷팅 처리
    public static AioeResponseDTO toResponseDto(AiOeChatMessage aiOeChatMessage) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = aiOeChatMessage.getDateTime().format(formatter);

        return AioeResponseDTO.builder()
                .message(aiOeChatMessage.getMessage())
                .timestamp(formattedTimestamp)
                .build();
    }

    public static ChatHistoryDTO toChatHistoryDto(List<AiOeChatMessage> aiOeChatMessages) {
        List<ChatMessageDTO> messageDTOs = aiOeChatMessages.stream()
                .map(ChatMessageMapper::toDto)
                .collect(Collectors.toList());

        return ChatHistoryDTO.builder()
                .messages(messageDTOs)
                .build();
    }

    public static AiOe toAiOe(Member member) {
        return AiOe.builder()
                .member(member)
                .build();
    }
}
