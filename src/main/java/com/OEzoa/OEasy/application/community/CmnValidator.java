package com.OEzoa.OEasy.application.community;

import com.OEzoa.OEasy.application.community.DTO.Cmn.CmnBoardListRequestDTO;
import com.OEzoa.OEasy.domain.community.BoardCommentRepository;
import com.OEzoa.OEasy.domain.community.BoardRepository;
import com.OEzoa.OEasy.domain.community.OeBoard;
import com.OEzoa.OEasy.domain.community.OeBoardComment;
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
    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;
    public Member getMember(long pk){
        return memberRepository.findById(pk).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FIND_MEMBER));
    }

    public OeBoard getBoard(long id){
        return boardRepository.findById(id).orElseThrow(()->new GlobalException(GlobalExceptionCode.COMMUNITY_NOT_FIND));
    }
    public OeBoardComment getBoardComment(long id){
        return boardCommentRepository.findById(id).orElseThrow(()->new GlobalException(GlobalExceptionCode.COMMUNITY_COMMENT_NOT_FIND));
    }

    /**
     *
     * @param memberPk
     * @param boardPk
     * @return
     */
    public OeBoard myBoardCheck(long memberPk, long boardPk){
        Member member = getMember(memberPk);
        OeBoard board = getBoard(boardPk);
        return boardRepository.findByMemberAndBoardPk(member, boardPk).orElseThrow(()-> new GlobalException(GlobalExceptionCode.COMMUNITY_NOT_FOUND_BOARD));
    }

    public OeBoardComment myCommentCheck(long memberPk, long commentPk){
        Member member = getMember(memberPk);
        OeBoardComment comment = getBoardComment(commentPk);

        return boardCommentRepository.findByBoardCommentPkAndMember(commentPk,member).orElseThrow(()-> new GlobalException(GlobalExceptionCode.BAD_REQUEST));

    }

    public void pageCheck(CmnBoardListRequestDTO dto){
        if(!dto.getSearchType().equals("titleAndContent") &&
                !dto.getSearchType().equals("title") &&
                !dto.getSearchType().equals("nickname")){
            throw new GlobalException(GlobalExceptionCode.COMMUNITY_INVALID_SEARCH_TYPE);
        }else if(!dto.getSortKeyword().equals("likeCnt") &&
                !dto.getSortKeyword().equals("boardPk")){
            throw new GlobalException(GlobalExceptionCode.COMMUNITY_INVALID_SORT_KEYWORD);
        }
    }

    public void sizeValueCheck(int size){
        if(size <= 0){
            throw new GlobalException(GlobalExceptionCode.COMMUNITY_INVALID_SIZE);
        }
    }

}
