package com.OEzoa.OEasy.application.recipe.DTO;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class GetRecipeManualDTO {
    private String content;
    private int order;
}
