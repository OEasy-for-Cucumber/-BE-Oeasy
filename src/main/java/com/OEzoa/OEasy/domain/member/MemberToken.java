package com.OEzoa.OEasy.domain.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_token")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MemberToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberPk;

    @OneToOne
    @MapsId
    @JoinColumn(name = "member_pk")
    private Member member;

    @Column(name = "access_tk")
    private String accessToken;

    @Column(name = "refresh_tk")
    private String refreshToken;
}
