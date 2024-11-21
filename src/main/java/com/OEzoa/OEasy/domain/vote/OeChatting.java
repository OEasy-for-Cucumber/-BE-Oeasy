package com.OEzoa.OEasy.domain.vote;

import com.OEzoa.OEasy.application.vote.DTO.ChattingResponseDTO;
import com.OEzoa.OEasy.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@Entity
@Table(name = "oe_chatting", schema = "oeasy")
@NoArgsConstructor
@AllArgsConstructor
public class OeChatting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_pk", nullable = false)
    private Long chattingPk;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Column(name = "chatting_timestamp")
    private LocalDateTime chattingTimestamp;

    @ManyToOne
    @JoinColumn(name = "member_pk", nullable = false)
    private Member member;

    public static ChattingResponseDTO of(Member member, String content) {
        return ChattingResponseDTO.builder()
                .id(member.getMemberPk())
                .content(content)
                .nickname(member.getNickname())
                .profileImg(member.getMemberImage())
                .build();
    }
}
