package com.OEzoa.OEasy.domain.recipe;

import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeManualDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "oe_recipe_manual", schema = "oeasy")
public class OeRecipeManual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_manual_pk", nullable = false)
    private Long recipeImgPk;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "manual_order", nullable = false)
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_pk")
    private OeRecipe recipe;

    public static GetRecipeManualDTO of(OeRecipeManual manual) {
        return GetRecipeManualDTO.builder()
                .content(manual.getContent())
                .order(manual.getOrder())
                .build();
    }

}