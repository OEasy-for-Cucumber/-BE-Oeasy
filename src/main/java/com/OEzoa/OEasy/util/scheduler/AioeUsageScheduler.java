package com.OEzoa.OEasy.util.scheduler;

import com.OEzoa.OEasy.domain.aioe.AioeUsageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AioeUsageScheduler {

    private final AioeUsageRepository aioeUsageRepository;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정
    public void resetUsageCounts() {
        aioeUsageRepository.deleteAll();
        log.info("모든 AIOE 사용횟수 초기화되었습니다.");
    }
}
