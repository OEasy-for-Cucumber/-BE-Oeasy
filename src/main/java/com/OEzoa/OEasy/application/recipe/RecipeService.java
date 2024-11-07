package com.OEzoa.OEasy.application.recipe;

import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeDTO;
import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeManualDTO;
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
    public GetRecipeDTO getRecipe(long id){
        OeRecipe recipe = oeRecipeRepository.findById(id).orElseThrow(() -> new GlobalException(GlobalExceptionCode.RECIPE_ID_NOT_FOUND));
        List<GetRecipeManualDTO> list = new ArrayList<>();
        for(OeRecipeManual manual: recipe.getRecipeManuals()){
            list.add(OeRecipeManual.of(manual));
        }

        return OeRecipe.of(recipe, list);
    }

}
