package com.OEzoa.OEasy.application.recipe;

import com.OEzoa.OEasy.application.recipe.DTO.*;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.recipe.*;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.util.timeTrace.TimeTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

@TimeTrace
@Service
@RequiredArgsConstructor
public class RecipeService {


    private final OeRecipeRepository oeRecipeRepository;
    private final OeRecipeLikeRepository oeRecipeLikeRepository;
    private final RecipeValidator recipeValidator;

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

        return GetRecipeResponseDTO.of(recipe, list);
    }

    @Transactional(readOnly = true)
    public Long getRandomRecipe(){
        return oeRecipeRepository.findRandomPk();
    }

    @Transactional(readOnly = true)
    public GetRecipeResponseBoardAllDTD getRecipeBoard(int page, int view){
        List<GetRecipeResponseBoardDTO> recipe = oeRecipeRepository.findByPage(PageRequest.of(
                page - 1,
                view,
                Sort.by(Sort.Direction.DESC, "recipePk")));
        if(recipe.isEmpty()) return new GetRecipeResponseBoardAllDTD();
        int cnt = (int)oeRecipeRepository.count();
        int total = cnt/view;
        total += cnt % view == 0 ? 0 : 1;

        return GetRecipeResponseBoardAllDTD.of(recipe, page, total, view);
    }

    public List<GetRecipeResponseBoardDTO> getRecipeLikedBoard(Member member){
        return oeRecipeRepository.findByMyLiked(member);
    }

    public boolean recipeLike(OeRecipe recipe, Member member){
        Optional<OeRecipeLike> optional = oeRecipeLikeRepository.findByRecipeAndMember(recipe, member);
        if(optional.isPresent()){
            oeRecipeLikeRepository.delete(optional.get());
            return false;
        }else{
            oeRecipeLikeRepository.save(OeRecipeLike.builder()
                    .recipe(recipe)
                    .member(member)
                    .timestamp(LocalDateTime.now())
                    .build());
            return true;
        }

    }

    public boolean recipeLikeCheck(OeRecipe recipe, Member member){
        return oeRecipeLikeRepository.existsByRecipeAndMember(recipe, member);
    }

    public List<GetRecipeBoardLikesResponseDTO> getRecipeBoardLikes(Member member, List<Long> list){
        List<GetRecipeBoardLikesResponseDTO> likes = new ArrayList<>();
        for(Long id: list){
            likes.add(GetRecipeBoardLikesResponseDTO.builder()
                            .recipePk(id)
                            .liked(recipeLikeCheck(recipeValidator.getRecipeById(id), member))
                    .build());
        }
        return likes;
    }




}
