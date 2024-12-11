package com.OEzoa.OEasy.domain.recipe;

import com.OEzoa.OEasy.domain.community.OeBoard;
import com.OEzoa.OEasy.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OeRecipeLikeRepository extends JpaRepository<OeRecipeLike, Long> {
    Optional<OeRecipeLike> findByRecipeAndMember(OeRecipe recipe, Member member);
}
