package com.OEzoa.OEasy.application.community.DTO.CmnComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCommentResponseDTO {
    private List<GetAllCommentDTO> contents;
    private Long totalElements;
    private int totalPages;
    private int size;

    public static GetAllCommentResponseDTO of(Page<GetAllCommentDTO> page) {
        return GetAllCommentResponseDTO.builder()
                .contents(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .size(page.getSize())
                .build();
    }
}
