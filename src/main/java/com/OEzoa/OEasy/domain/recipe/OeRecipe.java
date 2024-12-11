package com.OEzoa.OEasy.domain.recipe;

import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeResponseDTO;
import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeManualResponseDTO;
import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeResponseBoardAllDTD;
import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeResponseBoardDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "oe_recipe", schema = "oeasy")
public class OeRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_pk", nullable = false)
    private Long recipePk;

    @Column(name = "ingredients", nullable = false, length = 1000)
    private String ingredients;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "recipe_tip", nullable = false, length = 255)
    private String tip;

    @Column(name = "recipe_img", nullable = false, length = 255)
    private String img;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OeRecipeManual> recipeManuals;

    public static GetRecipeResponseDTO of(OeRecipe recipe, List<GetRecipeManualResponseDTO> list) {
        return GetRecipeResponseDTO.builder()
                .title(recipe.title)
                .ingredients(recipe.ingredients)
                .tip(recipe.tip)
                .manualList(list)
                .recipeImg(recipe.img)
                .build();
    }




}
