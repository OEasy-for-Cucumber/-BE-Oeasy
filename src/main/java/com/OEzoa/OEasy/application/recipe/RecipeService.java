package com.OEzoa.OEasy.application.recipe;

import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeResponseDTO;
import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeManualResponseDTO;
import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeBoardRequest;
import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeResponseBoardAll;
import com.OEzoa.OEasy.domain.recipe.OeRecipe;
import com.OEzoa.OEasy.domain.recipe.OeRecipeManual;
import com.OEzoa.OEasy.domain.recipe.OeRecipeRepository;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.util.timeTrace.TimeTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@TimeTrace
@Service
@RequiredArgsConstructor
public class RecipeService {


    private final OeRecipeRepository oeRecipeRepository;

    @Transactional(readOnly = true)
    public List<String> getRandomImg(int limit){
        return oeRecipeRepository.getRandomImg(limit);
    }

    @Transactional(readOnly = true)
    public GetRecipeResponseDTO getRecipe(long id){
        OeRecipe recipe = oeRecipeRepository.findById(id).orElseThrow(() -> new GlobalException(GlobalExceptionCode.RECIPE_ID_NOT_FOUND));
        List<GetRecipeManualResponseDTO> list = new ArrayList<>();
        for(OeRecipeManual manual: recipe.getRecipeManuals()){
            list.add(OeRecipeManual.of(manual));
        }

        return OeRecipe.of(recipe, list);
    }

    @Transactional(readOnly = true)
    public GetRecipeResponseBoardAll getRecipeBoard(long refId, int view){
        List<OeRecipe> recipe = oeRecipeRepository.findByRecipePkLessThanOrderByDescTopN(refId, view);
        if(recipe.isEmpty())
            return new GetRecipeResponseBoardAll();
        return OeRecipe.of(recipe);
    }

}
