package com.OEzoa.OEasy.domain.community;

import com.OEzoa.OEasy.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oe_board_comment", schema = "oeasy")
public class OeBoardComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_comment_pk", nullable = false)
    private Long boardCommentPk;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "content", length = 5000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "board_pk", nullable = false)
    private OeBoard board;

    @ManyToOne
    @JoinColumn(name = "member_pk", nullable = false)
    private Member member;
}
