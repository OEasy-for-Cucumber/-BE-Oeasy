package com.OEzoa.OEasy.application.recipe.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GetRecipeResponseBoardAll {

    private long refId;
    private List<GetRecipeResponseBoard> list;

}
