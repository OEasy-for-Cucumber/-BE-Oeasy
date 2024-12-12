package com.OEzoa.OEasy.application.recipe.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetRecipeBoardLikesResponseDTO {
    private long recipePk;
    private boolean liked;

}
