package com.OEzoa.OEasy.application.recipe.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GetRecipeResponseBoardDTO {
    private long id;
    private String title;
    private String imgUrl;
    private long like;
}
