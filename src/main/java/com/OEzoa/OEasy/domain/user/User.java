package com.OEzoa.OEasy.domain.user;

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
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder(toBuilder = true)
@Table(name = "user", schema = "oeasy")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pk", nullable = false)
    private Long userPk;

    @Column(name = "kakao_id", unique = true)
    private Long kakaoId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "pw", nullable = false)
    private String pw;

    @Column(name = "nick_name", length = 16)
    private String nickName;

    @Column(name = "user_image")
    private String userImage;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<OeBoard> oeBoard;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<OeBoardComment> oeBoardComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<OeBoardLike> oeBoardLikes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<OeVoting> oeVoting;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<OeChatting> oeChatting;
}
