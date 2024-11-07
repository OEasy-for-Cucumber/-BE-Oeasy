package com.OEzoa.OEasy.api.recipe;


import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeDTO;
import com.OEzoa.OEasy.application.recipe.RecipeService;
import com.OEzoa.OEasy.application.recipe.RecipeUploadService;
import com.OEzoa.OEasy.application.recipe.RecipeValidator;
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

    @GetMapping("/{limit}")
    @Operation(summary = "랜덤 이미지 불러오기", description = "limit에 해당하는 만큼 랜덤으로 불러옵니다.")
    public ResponseEntity<List<String>> getRandomImg(@PathVariable int limit) {
        return ResponseEntity.ok(recipeService.getRandomImg(limit));
    }

    @GetMapping()
    @Operation(summary = "레시피 가져오기", description = "id에 해당하는 데이터를 불러옵니다.")
    public ResponseEntity<GetRecipeDTO> getRecipe(@RequestParam long id) {
        recipeValidator.isValidValue(id);

        return ResponseEntity.ok(recipeService.getRecipe(id));
    }



//    @GetMapping("upload")
//    public RecipeDataXML uploadRecipe() {
//        return recipeUploadService.fetchAndSaveRecipes();
//    }
}
