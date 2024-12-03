package com.OEzoa.OEasy.application.vote.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VoteStatusRequestDTO {
    private Long hate;
    private Long like;
}
