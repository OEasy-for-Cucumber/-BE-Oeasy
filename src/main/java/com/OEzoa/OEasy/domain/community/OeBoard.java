package com.OEzoa.OEasy.domain.community;

import com.OEzoa.OEasy.application.community.CmnCreateRequestDTO;
import com.OEzoa.OEasy.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@Entity
@Table(name = "oe_board", schema = "oeasy")
@AllArgsConstructor
@NoArgsConstructor
public class OeBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_pk", nullable = false)
    private Long boardPk;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "content", length = 5000)
    private String content;

    @Column(name = "board_timestamp")
    private LocalDateTime boardTimestamp;

    @ManyToOne
    @JoinColumn(name = "member_pk", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<OeBoardComment> comments;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<OeBoardLike> likes;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<OeBoardImg> images;


    public static OeBoard of(CmnCreateRequestDTO cmn, Member member){
        return OeBoard.builder()
                .member(member)
                .title(cmn.getTitle())
                .boardTimestamp(LocalDateTime.now())
                .content(cmn.getContent())
                .build();
    }
}
