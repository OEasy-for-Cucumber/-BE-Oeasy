package com.OEzoa.OEasy.domain.community;

import com.OEzoa.OEasy.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "oe_board", schema = "oeasy")
public class OeBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_pk", nullable = false)
    private Long boardPk;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "content", length = 5000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_pk", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<OeBoardComment> comments;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<OeBoardLike> likes;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<OeBoardImg> images;
}
