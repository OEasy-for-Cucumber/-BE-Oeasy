package com.OEzoa.OEasy.application.recipe.DTO;

import com.OEzoa.OEasy.domain.recipe.OeRecipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    public static GetRecipeResponseBoardAllDTD of(List<GetRecipeResponseBoardDTO> Oelist, int nowPage, int totalPage, int view) {

        return GetRecipeResponseBoardAllDTD.builder()
                .hasNextPage(nowPage<totalPage)
                .lastPage(totalPage)
                .view(view)
                .nowPage(nowPage)
                .list(Oelist)
                .build();
    }
}
