package com.OEzoa.OEasy.application.member.mapper;

import com.OEzoa.OEasy.application.member.dto.KakaoDTO;
import com.OEzoa.OEasy.application.member.dto.MemberLoginResponseDTO;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberToken;
import org.springframework.stereotype.Component;

@Component
public class KakaoMapper {

    // 카카오 정보를 기반으로 Member 엔티티 생성
    public Member toMember(KakaoDTO kakaoInfo) {
        return Member.builder()
                .kakaoId(kakaoInfo.getId())
                .email(kakaoInfo.getEmail())
                .nickname(kakaoInfo.getNickname())
                .build();
    }

    // 로그인 응답 DTO 생성
    public MemberLoginResponseDTO toLoginResponseDTO(Member member, String accessToken, String refreshToken) {
        return MemberLoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    // MemberToken 생성
    public MemberToken createMemberToken(Member member, String accessToken, String refreshToken) {
        return MemberToken.builder()
                .member(member)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // MemberToken 업데이트
    public MemberToken updateMemberToken(MemberToken memberToken, String accessToken, String refreshToken) {
        return memberToken.toBuilder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
