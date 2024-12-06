package com.OEzoa.OEasy.application.member.mapper;

import com.OEzoa.OEasy.application.member.dto.MemberDTO;
import com.OEzoa.OEasy.application.member.dto.MemberLoginResponseDTO;
import com.OEzoa.OEasy.application.member.dto.MemberSignUpResponseDTO;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberToken;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberSignUpResponseDTO toSignUpResponseDTO(Member member) {
        return new MemberSignUpResponseDTO(member.getNickname());
    }

    public static MemberDTO toDto(Member member) {
        return MemberDTO.builder()
                .memberPk(member.getMemberPk())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .memberImage(member.getMemberImage())
                .kakaoId(member.getKakaoId())
                .build();
    }

    public MemberToken createMemberToken(Member member, String accessToken, String refreshToken) {
        return MemberToken.builder()
                .member(member)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public MemberToken updateMemberToken(MemberToken memberToken, String accessToken, String refreshToken) {
        return memberToken.toBuilder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Member toEntity(String email, String nickname, String hashedPassword, String salt) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .pw(hashedPassword)
                .salt(salt)
                .build();
    }

    public MemberLoginResponseDTO toLoginResponseDTO(Member member, String accessToken, String refreshToken) {
        return MemberLoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    public static Member updateNickname(Member member, String newNickname) {
        return member.toBuilder()
                .nickname(newNickname.trim())
                .build();
    }
    public Member updatePassword(Member member,  String newSalt, String newHashedPassword) {
        return member.toBuilder()
                .salt(newSalt)
                .pw(newHashedPassword)
                .build();
    }
}
