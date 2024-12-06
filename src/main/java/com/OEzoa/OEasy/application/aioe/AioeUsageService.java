package com.OEzoa.OEasy.application.aioe;

import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.domain.aioe.AioeUsage;
import com.OEzoa.OEasy.domain.aioe.AioeUsageRepository;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.exception.GlobalException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AioeUsageService {

    private final AioeUsageRepository aioeUsageRepository;

    private static final int MAX_USAGE_PER_DAY = 10;

    @Transactional
    public void validateAndIncrementUsage(Member member) {
        LocalDate today = LocalDate.now();

        // 오늘 날짜의 사용 기록 조회
        AioeUsage usage = aioeUsageRepository.findByMemberAndUsageDate(member, today)
                .orElseGet(() -> {
                    // 새로운 사용 기록 생성
                    AioeUsage newUsage = AioeUsage.builder()
                            .member(member)
                            .usageDate(today)
                            .usageCount(0)
                            .build();
                    return aioeUsageRepository.save(newUsage);
                });

        // 사용 횟수 초과 확인
        if (usage.getUsageCount() >= MAX_USAGE_PER_DAY) {
            log.error("[AIOE🥒 사용 제한 초과] Member ID: {}", member.getMemberPk());
            throw new GlobalException(GlobalExceptionCode.MAX_USAGE_EXCEEDED);
        }

        // 사용 횟수 증가
        usage.setUsageCount(usage.getUsageCount() + 1);
        aioeUsageRepository.save(usage);
    }
}
