package com.OEzoa.OEasy.domain.community;

import com.OEzoa.OEasy.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oe_board_comment", schema = "oeasy")
public class OeBoardComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_comment_pk", nullable = false)
    private Long boardCommentPk;

    @Column(name = "content", length = 5000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "board_pk", nullable = false)
    private OeBoard board;

    @ManyToOne
    @JoinColumn(name = "member_pk", nullable = false)
    private Member member;
}
