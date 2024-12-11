package com.OEzoa.OEasy.application.aioe;

import com.OEzoa.OEasy.application.aioe.dto.AioeIntroMessageDTO;
import com.OEzoa.OEasy.application.aioe.dto.AioeResponseDTO;
import com.OEzoa.OEasy.application.aioe.dto.ChatHistoryDTO;
import com.OEzoa.OEasy.application.aioe.mapper.ChatMessageMapper;
import com.OEzoa.OEasy.application.aioe.validator.AioeValidator;
import com.OEzoa.OEasy.application.member.TokenValidator;
import com.OEzoa.OEasy.domain.aioe.AiOe;
import com.OEzoa.OEasy.domain.aioe.AioeRepository;
import com.OEzoa.OEasy.domain.aioe.AiOeChatMessage;
import com.OEzoa.OEasy.domain.aioe.ChatMessageRepository;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.infra.api.aioe.OpenAIClient;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AioeService {

    @Autowired
    private  TokenValidator tokenValidator;
    @Autowired
    private  AioeRepository aioeRepository;
    @Autowired
    private  ChatMessageRepository chatMessageRepository;
    @Autowired
    private  OpenAIClient openAIClient;
    @Autowired
    private  AioeValidator aioeValidator;
    @Autowired
    private AioeUsageService aioeUsageService;

    // 챗봇 시작 로직
    @Transactional
    public AioeIntroMessageDTO startChatbot(String accessToken) {
        Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);

        // 이미 챗봇이 연결되어 있는 경우 기존 데이터를 반환
        Optional<AiOe> existingAiOe = aioeRepository.findByMember(member);
        if (existingAiOe.isPresent()) {
            AiOe aiOe = existingAiOe.get();

            // 기존 초기 메시지 검색
            AiOeChatMessage initialMessage = chatMessageRepository.findFirstByAiOeAndTypeOrderByDateTimeAsc(aiOe, "aioe");
            if (initialMessage != null) {
                return ChatMessageMapper.toStartResponseDto(initialMessage);
            }
        }

        // 새로운 챗봇 연결 생성
        AiOe aiOe = ChatMessageMapper.toAiOe(member);
        aioeRepository.save(aiOe);

        // 초기 메시지 생성 및 저장
        AiOeChatMessage initialMessage = ChatMessageMapper.toEntity(
                "안녕하세오이? 저는 AI 오이입니다오이! 오이에 관련된 질문을 해주세오이! 🥒", "aioe", aiOe
        );
        chatMessageRepository.save(initialMessage);
        return ChatMessageMapper.toStartResponseDto(initialMessage);
    }

    // 응답이랑 시간 생성~
    @Transactional
    public AioeResponseDTO handleUserQuestionWithTimestamp(String question, String accessToken) {
        Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);

        // 검증
        AiOe aiOe = aioeValidator.validateChatbotConnection(member);
        aioeValidator.validateQuestionLength(question);
        aioeValidator.validateQuestionContent(question);

        // 횟수 검증 및 증가
        aioeUsageService.validateAndIncrementUsage(member);
        // 사용자 질문 저장
        AiOeChatMessage userMessage = ChatMessageMapper.toEntity(question, "user", aiOe);
        chatMessageRepository.save(userMessage);

        String gptResponse = openAIClient.askQuestion(question);
        AiOeChatMessage gptMessage = ChatMessageMapper.toEntity(gptResponse, "aioe", aiOe);
        chatMessageRepository.save(gptMessage);

        return ChatMessageMapper.toResponseDto(gptMessage);
    }

    // 대화 내용 조회
    @Transactional(readOnly = true)
    public ChatHistoryDTO getChatHistory(String accessToken) {
        Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);
        AiOe aiOe = aioeValidator.validateChatbotConnection(member);

        List<AiOeChatMessage> aiOeChatMessages = chatMessageRepository.findByAiOeOrderByDateTimeAsc(aiOe);
        return ChatMessageMapper.toChatHistoryDto(aiOeChatMessages);
    }

    // 대화 내용 삭제
    @Transactional
    public String deleteChatbotConnection(String accessToken) {
        Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);

        // 챗봇 연결 여부 확인
        Optional<AiOe> optionalAiOe = aioeRepository.findByMember(member);
        if (optionalAiOe.isEmpty()) {
            return "삭제할 데이터가 없습니다."; // 연결이 없는 경우 메시지 반환
        }

        AiOe aiOe = optionalAiOe.get();
        chatMessageRepository.deleteByAiOe(aiOe); // 챗봇 대화 메시지 삭제
        aioeRepository.delete(aiOe);             // 챗봇 연결 삭제

        return "채팅 로그와 챗봇 연결이 삭제되었습니다.";
    }
}
