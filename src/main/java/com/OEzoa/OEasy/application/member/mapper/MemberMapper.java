package com.OEzoa.OEasy.application.member.mapper;

import com.OEzoa.OEasy.application.member.dto.MemberDTO;
import com.OEzoa.OEasy.domain.member.Member;

public class MemberMapper {

    public static MemberDTO toDto(Member member) {
        return MemberDTO.builder()
                .memberPk(member.getMemberPk())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .memberImage(member.getMemberImage())
                .kakaoId(member.getKakaoId())
                .build();
    }

    public static Member toEntity(MemberDTO memberDTO) {
        return Member.builder()
                .memberPk(memberDTO.getMemberPk())
                .email(memberDTO.getEmail())
                .nickname(memberDTO.getNickname())
                .memberImage(memberDTO.getMemberImage())
                .build();
    }

    public static Member updateNickname(Member member, String newNickname) {
        return member.toBuilder()
                .nickname(newNickname.trim())
                .build();
    }
}
