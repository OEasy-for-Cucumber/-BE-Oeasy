package com.OEzoa.OEasy.util.scheduler;

import com.OEzoa.OEasy.application.graph.GraphService;
import com.OEzoa.OEasy.application.graph.dto.GraphRequestDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GraphScheduler {

    private final GraphService graphService;

    @Scheduled(cron = "0 0 6 * * ?") // 매일 오전 6시
    public void scheduleCucumberDataFetch() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        GraphRequestDTO requestDTO = new GraphRequestDTO(today, today);
        graphService.updateCucumberPriceData(requestDTO);
    }
}
