package com.OEzoa.OEasy.domain.recipe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "oe_recipe", schema = "oeasy")
public class OeRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_pk", nullable = false)
    private Long recipePk;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "recipe_timestamp", nullable = false)
    private String recipeTimestamp;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<OeRecipeImg> recipeImages;
}
