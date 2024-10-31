package com.OEzoa.OEasy.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_token")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPk;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_pk")
    private User user;

    @Column(name = "access_tk")
    private String accessToken;

    @Column(name = "refresh_tk")
    private String refreshToken;
}
