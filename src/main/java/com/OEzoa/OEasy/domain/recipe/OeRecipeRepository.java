package com.OEzoa.OEasy.domain.recipe;

import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeResponseBoardAllDTD;
import com.OEzoa.OEasy.application.recipe.DTO.GetRecipeResponseBoardDTO;
import com.OEzoa.OEasy.domain.member.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OeRecipeRepository extends JpaRepository<OeRecipe, Long> {
    @Query(value = "SELECT recipe_img FROM oe_recipe ORDER BY RAND() limit :limit", nativeQuery = true)
    List<String> getRandomImg(@Param("limit") int limit);

    long count();

    Integer deleteByImgLike(String img);



    List<OeRecipe> findAllByOrderByRecipePkDesc(Pageable pageable);

    @Query(value = "SELECT new com.OEzoa.OEasy.application.recipe.DTO.GetRecipeResponseBoardDTO(" +
            "or.recipePk," +
            "or.title," +
            "or.img," +
            "(SELECT count(*) " +
            "FROM OeRecipeLike orl " +
            "WHERE orl.recipe = or )) " +
            "FROM OeRecipe or")
    List<GetRecipeResponseBoardDTO> findByPage(Pageable pageable);

    @Query(value = "select recipe_pk from oe_recipe order by recipe_pk desc limit 1", nativeQuery = true)
    long findTopId();

    @Query(value = "SELECT * FROM oe_recipe order by RAND() limit 1", nativeQuery = true)
    OeRecipe findRandomRecipe();

    @Query(value = "SELECT recipe_pk from oe_recipe ORDER BY RAND() limit 1", nativeQuery = true)
    long findRandomPk();

    @Query(value = "SELECT new com.OEzoa.OEasy.application.recipe.DTO.GetRecipeResponseBoardDTO(" +
            "orl.recipe.recipePk," +
            "orl.recipe.title," +
            "orl.recipe.img," +
            "(SELECT count(*) " +
                "FROM OeRecipeLike or " +
                "WHERE or = orl )) " +
            "FROM OeRecipeLike orl " +
            "WHERE orl.member = :member")
    List<GetRecipeResponseBoardDTO> findByMyLiked(@Param("member") Member member);
}
