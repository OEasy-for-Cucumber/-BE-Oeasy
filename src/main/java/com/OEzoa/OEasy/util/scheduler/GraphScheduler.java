package com.OEzoa.OEasy.util.scheduler;

import com.OEzoa.OEasy.application.graph.GraphService;
import com.OEzoa.OEasy.application.graph.dto.GraphRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class GraphScheduler {

    private final GraphService graphService;

    @Scheduled(cron = "0 0 6 * * ?") // 매일 오전 6시
    public void scheduleCucumberDataFetch() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 평균 데이터 업데이트
        GraphRequestDTO averageRequestDTO = new GraphRequestDTO(today, today);
        try {
            graphService.updateAveragePrice(averageRequestDTO);
            log.info("평균 데이터 업데이트 성공: {}", today);
        } catch (Exception e) {
            log.error("평균 데이터 업데이트 중 오류 발생: {}", e.getMessage(), e);
        }

        // 지역 데이터 업데이트
        try {
            graphService.updateRegionalPrice(today);
            log.info("지역 데이터 업데이트 성공: {}", today);
        } catch (Exception e) {
            log.error("지역 데이터 업데이트 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
