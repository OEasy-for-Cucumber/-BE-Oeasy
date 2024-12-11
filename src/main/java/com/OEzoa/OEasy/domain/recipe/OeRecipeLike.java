package com.OEzoa.OEasy.domain.recipe;

import com.OEzoa.OEasy.domain.community.OeBoard;
import com.OEzoa.OEasy.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oe_recipe_like",schema = "oeasy")
public class OeRecipeLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oe_recipe_like_pk")
    private long likePk;

    @ManyToOne
    @JoinColumn(name = "member_pk", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "recipe_pk", nullable = false)
    private OeRecipe recipe;

    @Column(name = "recipe_timestamp")
    private LocalDateTime timestamp;
}
