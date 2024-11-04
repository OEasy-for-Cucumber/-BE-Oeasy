package com.OEzoa.OEasy.domain.voting;

import com.OEzoa.OEasy.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oe_voting", schema = "oeasy")
public class OeVoting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voting_pk", nullable = false)
    private Long votingPk;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "voting", nullable = false)
    private Boolean voting;

    @ManyToOne
    @JoinColumn(name = "member_pk", nullable = false)
    private Member member;
}
