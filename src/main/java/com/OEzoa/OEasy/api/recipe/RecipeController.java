package com.OEzoa.OEasy.api.recipe;


import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeResponseDTO;
import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeResponseBoardAllDTD;
import com.OEzoa.OEasy.application.recipe.RecipeService;
import com.OEzoa.OEasy.application.recipe.RecipeValidator;
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

    @GetMapping()
    @Operation(summary = "레시피 가져오기", description = "id에 해당하는 데이터를 불러옵니다.")
    public ResponseEntity<GetRecipeResponseDTO> getRecipe(@RequestParam long id) {
        recipeValidator.isValidValue(id);

        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @GetMapping("board")
    @Operation(summary = "레시피 보드 가져오기",
            description = "refId 보다 작은 값 들을 조회하여 view 만큼 가져오고 더 이상 가져올 값이 없다면 list는 null을 반환합니다")
    public ResponseEntity<GetRecipeResponseBoardAllDTD> getRecipeBoard(
            @RequestParam("page") int page,
            @RequestParam("view") int view) {
        recipeValidator.isValidValue(page,view);
        return  ResponseEntity.ok(recipeService.getRecipeBoard(page, view));
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
