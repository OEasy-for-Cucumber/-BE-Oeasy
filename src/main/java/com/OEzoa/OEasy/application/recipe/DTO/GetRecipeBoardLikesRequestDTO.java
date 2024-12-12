package com.OEzoa.OEasy.application.recipe.DTO;

import lombok.Getter;

import java.util.List;

@Getter
public class GetRecipeBoardLikesRequestDTO {
    private int memberPk;
    private List<Long> recipeList;
}
