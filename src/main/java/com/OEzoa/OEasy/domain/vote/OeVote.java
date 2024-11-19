package com.OEzoa.OEasy.domain.vote;

import com.OEzoa.OEasy.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oe_vote", schema = "oeasy")
public class OeVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_pk", nullable = false)
    private Long votePk;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "vote", nullable = false)
    private Boolean vote;

    @ManyToOne
    @JoinColumn(name = "member_pk", nullable = false)
    private Member member;
}
