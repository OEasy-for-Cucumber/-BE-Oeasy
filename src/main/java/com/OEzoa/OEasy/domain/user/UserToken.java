package com.OEzoa.OEasy.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_token")
public class UserToken {
    @Id
    private Long userPk;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_pk")
    private User user;

    @Column(name = "access_tk")
    private String accessToken;

    @Column(name = "refresh_tk")
    private String refreshToken;

    @Column(name = "kakao_tk")
    private String kakaoToken;


    // Getters and setters
}