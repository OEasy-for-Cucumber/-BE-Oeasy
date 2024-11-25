package com.OEzoa.OEasy.application.community;

import com.OEzoa.OEasy.domain.community.BoardRepository;
import com.OEzoa.OEasy.domain.community.OeBoard;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CmnValidator {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    public Member getMember(long pk){
        System.out.println("pk = " + pk);
        return memberRepository.findById(pk).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FIND_MEMBER));
    }

    public OeBoard getBoard(long id){
        return boardRepository.findById(id).orElseThrow(()->new GlobalException(GlobalExceptionCode.COMMUNITY_NOT_FIND));
    }

    /**
     *
     * @param memberPk
     * @param boardPk
     * @return
     */
    public OeBoard myBoardCheck(long memberPk, long boardPk){
        Member member = getMember(memberPk);
        return boardRepository.findByMember(member).orElseThrow(()-> new GlobalException(GlobalExceptionCode.COMMUNITY_NOT_FOUND_BOARD));
    }


}
