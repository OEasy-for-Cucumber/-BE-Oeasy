package com.OEzoa.OEasy.application.recipe.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class GetRecipeDTO {
    private String recipeImg;
    @Schema(description = "재료들 입니다.", example = "●오이무침 :\n" +
            "오이 70g(1/3개), 다진 땅콩 10g(1큰술)\n" +
            "●순두부사과 소스 : \n" +
            "순두부 40g(1/8봉지), 사과 50g(1/3개)")
    private String ingredients;
    private String tip;
    private String title;
    private List<GetRecipeManualDTO> manualList = new ArrayList<>();
}
