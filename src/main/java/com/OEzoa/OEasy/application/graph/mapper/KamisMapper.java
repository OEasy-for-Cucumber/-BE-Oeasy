package com.OEzoa.OEasy.application.graph.mapper;

import com.OEzoa.OEasy.application.graph.dto.GraphResponseDTO;
import com.OEzoa.OEasy.domain.graph.OeGraph;

public class KamisMapper {

    public static OeGraph toEntity(String date, Long price) {
        return OeGraph.builder()
                .date(date)
                .price(price)
                .build();
    }

    public static GraphResponseDTO toDto(OeGraph oeGraph) {
        return new GraphResponseDTO(oeGraph.getDate(), oeGraph.getPrice());
    }
}
