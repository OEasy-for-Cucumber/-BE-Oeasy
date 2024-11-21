package com.OEzoa.OEasy.application.aioe;

import com.OEzoa.OEasy.application.aioe.dto.AioeResponseDTO;
import com.OEzoa.OEasy.application.aioe.dto.ChatHistoryDTO;
import com.OEzoa.OEasy.application.aioe.mapper.ChatMessageMapper;
import com.OEzoa.OEasy.application.aioe.validator.AioeValidator;
import com.OEzoa.OEasy.application.member.TokenValidator;
import com.OEzoa.OEasy.domain.aioe.AiOe;
import com.OEzoa.OEasy.domain.aioe.AioeRepository;
import com.OEzoa.OEasy.domain.aioe.ChatMessage;
import com.OEzoa.OEasy.domain.aioe.ChatMessageRepository;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.infra.api.aioe.OpenAIClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AioeService {

    private final TokenValidator tokenValidator;
    private final AioeRepository aioeRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final OpenAIClient openAIClient;
    private final AioeValidator aioeValidator;

    // 챗봇 시작 로직
    @Transactional
    public void startChatbot(String accessToken) {
        Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);

        if (aioeRepository.findByMember(member).isPresent()) {
            throw new GlobalException(GlobalExceptionCode.MEMBER_ALREADY_CONNECTED);
        }
        AiOe aiOe = ChatMessageMapper.toAiOe(member);
        aioeRepository.save(aiOe);

        ChatMessage initialMessage = ChatMessageMapper.toEntity(
                "안녕하세오이? 저는 AI 오이입니다오이! 오이에 관련된 질문을 해주세오이! 🥒", "aioe", aiOe
        );
        chatMessageRepository.save(initialMessage);
    }

    // 응답이랑 시간 생성~
    @Transactional
    public AioeResponseDTO handleUserQuestionWithTimestamp(String question, String accessToken) {
        Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);

        // 검증
        AiOe aiOe = aioeValidator.validateChatbotConnection(member);
        aioeValidator.validateQuestionLength(question);
        aioeValidator.validateQuestionContent(question);

        // 사용자 질문 저장
        ChatMessage userMessage = ChatMessageMapper.toEntity(question, "user", aiOe);
        chatMessageRepository.save(userMessage);


        String gptResponse = openAIClient.askQuestion(question);
        ChatMessage gptMessage = ChatMessageMapper.toEntity(gptResponse, "aioe", aiOe);
        chatMessageRepository.save(gptMessage);

        return ChatMessageMapper.toResponseDto(gptMessage);
    }

    // 대화 내용 조회
    @Transactional(readOnly = true)
    public ChatHistoryDTO getChatHistory(String accessToken) {
        Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);
        AiOe aiOe = aioeValidator.validateChatbotConnection(member);

        List<ChatMessage> chatMessages = chatMessageRepository.findByAiOeOrderByDateTimeAsc(aiOe);
        return ChatMessageMapper.toChatHistoryDto(chatMessages);
    }

    // 대화 내용 삭제
    @Transactional
    public void deleteChatHistory(String accessToken) {
        Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);
        AiOe aiOe = aioeValidator.validateChatbotConnection(member);

        chatMessageRepository.deleteByAiOe(aiOe);
    }
}
