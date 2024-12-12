package com.OEzoa.OEasy.api.recipe;


import com.OEzoa.OEasy.application.recipe.DTO.*;
import com.OEzoa.OEasy.application.recipe.RecipeService;
import com.OEzoa.OEasy.application.recipe.RecipeValidator;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.recipe.OeRecipe;
import com.OEzoa.OEasy.domain.recipe.OeRecipeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
@Tag(name = "OE Recipe API", description = "레시피 기능을 제공합니다.")
public class RecipeController {
    //private final RecipeUploadService recipeUploadService;
    private final RecipeService recipeService;
    private final RecipeValidator recipeValidator;

    private final OeRecipeRepository oeRecipeRepository;

    @GetMapping("/{limit}")
    @Operation(summary = "랜덤 이미지 불러오기", description = "limit에 해당하는 만큼 랜덤으로 불러옵니다.")
    public ResponseEntity<List<String>> getRandomImg(@PathVariable int limit) {
        return ResponseEntity.ok(recipeService.getRandomImg(limit));
    }

    @GetMapping
    @Operation(summary = "레시피 가져오기", description = "id에 해당하는 데이터를 불러옵니다.")// 좋아요 추가
    public ResponseEntity<GetRecipeResponseDTO> getRecipe(@RequestParam long id) {
        recipeValidator.isValidValue(id);

        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @GetMapping("/random")
    @Operation(summary = "유효한 레시피 pk를 랜덤으로 가져오기")
    public ResponseEntity<Long> getRandomRecipe() {

        return ResponseEntity.ok(recipeService.getRandomRecipe());
    }

    @GetMapping("board")
    @Operation(summary = "레시피 보드 가져오기",
            description = "refId 보다 작은 값 들을 조회하여 view 만큼 가져오고 더 이상 가져올 값이 없다면 list는 null을 반환합니다")//좋아요추가
    public ResponseEntity<GetRecipeResponseBoardAllDTD> getRecipeBoard(
            @RequestParam("page") int page,
            @RequestParam("view") int view) {
        recipeValidator.isValidValue(page,view);
        return  ResponseEntity.ok(recipeService.getRecipeBoard(page, view));
    }

    @PostMapping("board/like-check")
    @Operation(summary = "레시피 보드 좋아요 체크")//좋아요추가
    public List<GetRecipeBoardLikesResponseDTO> getRecipeBoardLikes(@RequestBody GetRecipeBoardLikesRequestDTO dto) {
        Member member = recipeValidator.getMemberById(dto.getMemberPk());
        return recipeService.getRecipeBoardLikes(member, dto.getRecipeList());
    }
    @GetMapping("board/{memberPk}")
    @Operation(summary = "좋아요 누른 게시물 가져오기")
    public List<GetRecipeResponseBoardDTO> getRecipeLikedBoard(@PathVariable long memberPk) {
        Member member = recipeValidator.getMemberById(memberPk);
        return recipeService.getRecipeLikedBoard(member);

    }

    @GetMapping("like/{memberPk}/{recipePk}")
    @Operation(summary = "좋아요"
            , description = "memberPk와 recipePk를 통해서 좋아요 기록을 조회하고 있다면 삭제하고 false(좋아요 취소), 없다면 기록을 저장하고 true(좋아요)를 반환")
    public boolean like(@PathVariable long memberPk, @PathVariable long recipePk) {
        OeRecipe recipe = recipeValidator.getRecipeById(recipePk);
        Member member = recipeValidator.getMemberById(memberPk);

        return recipeService.recipeLike(recipe, member);
    }

    @GetMapping("like-check/{memberPk}/{recipePk}")
    @Operation(summary = "게시글 좋아요 체크")
    public boolean likeCheck(@PathVariable long memberPk, @PathVariable long recipePk) {
        OeRecipe recipe = recipeValidator.getRecipeById(recipePk);
        Member member = recipeValidator.getMemberById(memberPk);
        return recipeService.recipeLikeCheck(recipe, member);
    }



//    @GetMapping("del")
//    @Transactional
//    public void asd(String str){
//        System.out.println("str = " + str);
//        StringTokenizer st = new StringTokenizer(str, ",");
//        while(st.hasMoreTokens()){
//            String s = st.nextToken().strip();
//            System.out.println("s = " + s);
//            System.out.println(oeRecipeRepository.deleteByImgLike("%"+s+"%"));
//        }
//    }


//    @GetMapping("upload")
//    public RecipeDataXML uploadRecipe() {
//        return recipeUploadService.fetchAndSaveRecipes();
//    }
}
