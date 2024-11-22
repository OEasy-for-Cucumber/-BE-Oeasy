package com.OEzoa.OEasy.application.vote.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteInitResponseDTO {

    private Long like;
    private Long hate;


}
