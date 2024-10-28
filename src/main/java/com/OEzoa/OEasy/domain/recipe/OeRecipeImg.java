package com.OEzoa.OEasy.domain.recipe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oe_recipe_img", schema = "oeasy")
public class OeRecipeImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_img_pk", nullable = false)
    private Long recipeImgPk;

    @Column(name = "recipe_img", nullable = false)
    private String recipeImg;

    @ManyToOne
    @JoinColumn(name = "recipe_pk", nullable = false)
    private OeRecipe recipe;
}
