package com.OEzoa.OEasy.application.graph;

import com.OEzoa.OEasy.application.graph.dto.GraphRequestDTO;
import com.OEzoa.OEasy.domain.graph.GraphRepository;
import com.OEzoa.OEasy.domain.graph.GraphRegionRepository;
import com.OEzoa.OEasy.domain.graph.OeGraph;
import com.OEzoa.OEasy.domain.graph.OeGraphRegion;
import com.OEzoa.OEasy.infra.api.KamisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GraphService {

    private final KamisService kamisService;
    private final GraphRepository graphRepository;
    private final GraphRegionRepository graphRegionRepository;
    /**
     * 평균 데이터 업데이트
     */
    @Transactional
    public void updateAveragePrice(GraphRequestDTO requestDTO) {
        kamisService.fetchAndUpdateAveragePriceData(requestDTO); // 지역 데이터 처리 및 저장
        log.info("평균 데이터 업데이트 완료.");
    }

    /**
     * 지역 데이터 업데이트
     */
    @Transactional
    public void updateRegionalPrice(String date) {
        kamisService.fetchAndUpdateRegionalPriceData(date); // 지역 데이터 처리 및 저장
        log.info("지역 데이터 업데이트 완료.");
    }

    /**
     * 평균 데이터 조회
     */
    @Transactional(readOnly = true)
    public List<OeGraph> getAveragePrice(String startDate, String endDate) {
        return graphRepository.findByDateBetween(startDate, endDate);
    }

    /**
     * 모든 지역의 최신 데이터 조회
     */
    public List<OeGraphRegion> getAllRegionalPrice() {
        return graphRegionRepository.findAll();
    }
}
