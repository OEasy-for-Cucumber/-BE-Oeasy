package com.OEzoa.OEasy.application.member.mapper;

import com.OEzoa.OEasy.domain.member.MemberToken;
import org.springframework.stereotype.Component;

@Component
public class MemberTokenMapper {

    public MemberToken updateToken(MemberToken memberToken, String newAccessToken) {
        return memberToken.toBuilder()
                .accessToken(newAccessToken)
                .build();
    }
}