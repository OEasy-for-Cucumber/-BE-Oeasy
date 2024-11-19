package com.OEzoa.OEasy.domain.member;

import com.OEzoa.OEasy.domain.chatting.OeChatting;
import com.OEzoa.OEasy.domain.community.OeBoard;
import com.OEzoa.OEasy.domain.community.OeBoardComment;
import com.OEzoa.OEasy.domain.community.OeBoardLike;
import com.OEzoa.OEasy.domain.voting.OeVoting;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder(toBuilder = true)
@Table(name = "member", schema = "oeasy")
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_pk", nullable = false)
    private Long memberPk;

    @Column(name = "kakao_id", unique = true)
    private Long kakaoId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "pw")
    private String pw;

    @Column(name = "salt")
    private String salt;

    @Column(name = "nick_name", length = 8)
    private String nickname;

    @Column(name = "member_image")
    private String memberImage;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true) // 회원 삭제 시 MemberToken 삭제
    private MemberToken memberToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true) // 게시글 삭제
    private List<OeBoard> oeBoard;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true) // 댓글 삭제
    private List<OeBoardComment> oeBoardComments;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true) // 좋아요 삭제
    private List<OeBoardLike> oeBoardLikes;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true) // 투표 삭제
    private List<OeVoting> oeVoting;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true) // 채팅 삭제
    private List<OeChatting> oeChatting;

}
