package com.OEzoa.OEasy.application.recipe;

import com.OEzoa.OEasy.domain.recipe.OeRecipeRepository;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RecipeValidator {

    private final OeRecipeRepository oeRecipeRepository;

    public void isValidValue(long id){
        long top = oeRecipeRepository.findTopId();

        if(id < 0 || top < id){
            throw new GlobalException(GlobalExceptionCode.RECIPE_OUT_OF_VALID_RANGE);
        }

    }

    public void isValidValue(int page,int view){
        if(page <= 0 || view <= 0){
            throw new GlobalException(GlobalExceptionCode.RECIPE_OUT_OF_VALID_RANGE);
        }

    }

}
