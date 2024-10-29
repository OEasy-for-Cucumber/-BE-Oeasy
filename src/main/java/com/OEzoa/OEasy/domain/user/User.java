package com.OEzoa.OEasy.domain.user;

import com.OEzoa.OEasy.domain.chatting.OeChatting;
import com.OEzoa.OEasy.domain.community.OeBoard;
import com.OEzoa.OEasy.domain.community.OeBoardComment;
import com.OEzoa.OEasy.domain.community.OeBoardLike;
import com.OEzoa.OEasy.domain.voting.OeVoting;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user", schema = "oeasy")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pk", nullable = false)
    private Long userPk;

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
