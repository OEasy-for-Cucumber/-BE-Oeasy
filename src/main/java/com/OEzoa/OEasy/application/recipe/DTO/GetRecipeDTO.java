package com.OEzoa.OEasy.application.recipe.DTO;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class GetRecipeDTO {
    private String recipeImg;
    private String ingredients;
    private String tip;
    private String title;
    private List<GetRecipeManualDTO> manualList = new ArrayList<>();
}
