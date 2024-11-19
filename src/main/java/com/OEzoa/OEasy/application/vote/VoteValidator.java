package com.OEzoa.OEasy.application.vote;

import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VoteValidator {

    private final MemberRepository memberRepository;

    public Member getMember(long pk){
        return memberRepository.findById(pk).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FIND_MEMBER));
    }

}
