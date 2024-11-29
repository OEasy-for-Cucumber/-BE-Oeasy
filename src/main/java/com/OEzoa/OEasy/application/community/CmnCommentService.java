package com.OEzoa.OEasy.application.community;

import com.OEzoa.OEasy.application.community.DTO.CmnComment.GetAllCommentResponseDTO;
import com.OEzoa.OEasy.domain.community.BoardCommentRepository;
import com.OEzoa.OEasy.domain.community.OeBoard;
import com.OEzoa.OEasy.domain.community.OeBoardComment;
import com.OEzoa.OEasy.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CmnCommentService {

    private final BoardCommentRepository boardCommentRepository;

    public GetAllCommentResponseDTO getAllComment(int page, int size, OeBoard board) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "boardCommentPk");
       return GetAllCommentResponseDTO.of(boardCommentRepository.findByBoard(board, pageable));
    }

    public GetAllCommentResponseDTO createComment(Member member, OeBoard board, String content, int size){
        boardCommentRepository.save(OeBoardComment.builder()
                        .content(content)
                        .board(board)
                        .member(member)
                        .boardCommentTimestamp(LocalDateTime.now())
                .build());
        return getAllComment(0, size, board);
    }

    public void deleteComment(OeBoardComment comment){
        boardCommentRepository.delete(comment);
    }

    public void updateComment(OeBoardComment comment, String content){
        boardCommentRepository.save(comment.toBuilder()
                        .content(content)
                .build());
    }

}
