package com.OEzoa.OEasy.application.community.DTO.Cmn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmnBoardListResponseDTO {

    private List<CmnBoardListDTO> contents;

    private long totalElements;
    private long totalPages;
    private long size;

    public static CmnBoardListResponseDTO of(Page<CmnBoardListDTO> dto){
        return CmnBoardListResponseDTO.builder()
                .contents(dto.getContent())
                .totalElements(dto.getTotalElements())
                .totalPages(dto.getTotalPages())
                .size(dto.getSize())
                .build();
    }

}
