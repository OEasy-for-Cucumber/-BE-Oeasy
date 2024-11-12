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
public class GetRecipeResponseBoardAllDTD {

    private int nowPage;
    private int lastPage;
    private int view;
    private boolean hasNextPage;
    private List<GetRecipeResponseBoardDTO> list;

}
