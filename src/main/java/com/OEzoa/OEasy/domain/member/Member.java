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

    @Column(name = "pw") // 소셜 로그인 이슈
    private String pw;

    @Column(name = "salt", nullable = false)
    private String salt;

    @Column(name = "nick_name", length = 16)
    private String nickname;

    @Column(name = "member_image")
    private String memberImage;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberToken memberToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<OeBoard> oeBoard;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<OeBoardComment> oeBoardComments;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<OeBoardLike> oeBoardLikes;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<OeVoting> oeVoting;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<OeChatting> oeChatting;

}
