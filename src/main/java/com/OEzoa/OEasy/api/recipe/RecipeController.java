package com.OEzoa.OEasy.api.recipe;


import com.OEzoa.OEasy.application.recipe.RecipeService;
import com.OEzoa.OEasy.application.recipe.RecipeUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeUploadService recipeUploadService;
    private final RecipeService recipeService;

    @GetMapping("/{limit}")
    public ResponseEntity<List<String>> getRandomImg(@PathVariable int limit) {
        return ResponseEntity.ok(recipeService.getRandomImg(limit));
    }


//    @GetMapping("upload")
//    public RecipeDataXML uploadRecipe() {
//        return recipeUploadService.fetchAndSaveRecipes();
//    }
}
