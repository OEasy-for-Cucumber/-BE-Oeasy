package com.OEzoa.OEasy.application.aioe.validator;

import com.OEzoa.OEasy.domain.aioe.AiOe;
import com.OEzoa.OEasy.domain.aioe.AioeRepository;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AioeValidator {

    private final AioeRepository aioeRepository;

    // 질문 길이 검증
    public void validateQuestionLength(String question) {
        if (question == null || question.trim().isEmpty()) {
            log.error("[검증 실패] 질문이 비어 있거나 공백입니다.");
            throw new GlobalException(GlobalExceptionCode.INVALID_INPUT);
        }
        if (question.length() > 100) {
            log.error("[검증 실패] 질문이 너무 깁니다. 길이: {}", question.length());
            throw new GlobalException(GlobalExceptionCode.QUESTION_TOO_LONG);
        }
    }

    // 오이랑 관련 있는지 검증
    public void validateQuestionContent(String question) {
        if (!question.contains("오이")) {
            log.error("[검증 실패] 질문에 '오이'가 포함되지 않았습니다.");
            throw new GlobalException(GlobalExceptionCode.INVALID_INPUT);
        }
    }

    // 챗봇 연결 여부 검증
    public AiOe validateChatbotConnection(Member member) {
        AiOe aiOe = aioeRepository.findByMember(member)
                .orElseThrow(() -> {
                    log.error("[검증 실패] 챗봇 연결되지 않은 사용자 )");
                    return new GlobalException(GlobalExceptionCode.MEMBER_NOT_FOUND);
                });
        return aiOe;
    }
}
