package com.OEzoa.OEasy.application.graph;

import com.OEzoa.OEasy.application.graph.dto.GraphRequestDTO;
import com.OEzoa.OEasy.application.graph.dto.GraphResponseDTO;
import com.OEzoa.OEasy.application.graph.mapper.KamisMapper;
import com.OEzoa.OEasy.domain.graph.GraphRepository;
import com.OEzoa.OEasy.domain.graph.OeGraph;
import com.OEzoa.OEasy.infra.api.KamisService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GraphService {

    private final KamisService kamisService;
    private final GraphRepository graphRepository;

    @Transactional
    public List<GraphResponseDTO> updateCucumberPriceData(GraphRequestDTO requestDTO) {
        List<OeGraph> data = kamisService.fetchCucumberPriceData(requestDTO);
        graphRepository.saveAll(data);

        return data.stream()
                .map(KamisMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GraphResponseDTO> getCucumberPriceData(String startDate, String endDate) {
        List<OeGraph> data = graphRepository.findByDateBetween(startDate, endDate);
        return data.stream()
                .map(KamisMapper::toDto)
                .collect(Collectors.toList());
    }
}