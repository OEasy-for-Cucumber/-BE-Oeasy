package com.OEzoa.OEasy.application.recipe;

import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.domain.recipe.OeRecipe;
import com.OEzoa.OEasy.domain.recipe.OeRecipeRepository;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RecipeValidator {

    private final OeRecipeRepository oeRecipeRepository;
    private final MemberRepository memberRepository;

    public void isValidValue(long id){
        long top = oeRecipeRepository.findTopId();

        if(id < 0 || top < id){
            throw new GlobalException(GlobalExceptionCode.RECIPE_OUT_OF_VALID_RANGE);
        }

    }

    public void isValidValue(int page,int view){
        long cnt = oeRecipeRepository.count();
        int lastPage = (int)cnt/view;
        lastPage += cnt % view == 0 ? 0 : 1;
        if(page <= 0 || view <= 0 || lastPage < page){
            throw new GlobalException(GlobalExceptionCode.RECIPE_OUT_OF_VALID_RANGE);
        }

    }

    public OeRecipe getRecipeById(long id){
        return oeRecipeRepository.findById(id).orElseThrow(()-> new GlobalException(GlobalExceptionCode.RECIPE_ID_NOT_FOUND));
    }
    public Member getMemberById(long id){
        return memberRepository.findById(id).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FIND_MEMBER));
    }

}
