package com.OEzoa.OEasy.domain.chatting;

import com.OEzoa.OEasy.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oe_chatting", schema = "oeasy")
public class OeChatting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_pk", nullable = false)
    private Long chattingPk;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Column(name = "chatting_timestamp")
    private String chattingTimestamp;

    @ManyToOne
    @JoinColumn(name = "member_pk", nullable = false)
    private Member member;
}
