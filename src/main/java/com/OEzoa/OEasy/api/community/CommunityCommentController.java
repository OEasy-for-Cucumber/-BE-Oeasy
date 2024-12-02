package com.OEzoa.OEasy.api.community;

import com.OEzoa.OEasy.application.community.CmnCommentService;
import com.OEzoa.OEasy.application.community.CmnValidator;
import com.OEzoa.OEasy.application.community.DTO.CmnComment.CreateCommentRequestDTO;
import com.OEzoa.OEasy.application.community.DTO.CmnComment.DeleteCommentRequestDTO;
import com.OEzoa.OEasy.application.community.DTO.CmnComment.GetAllCommentResponseDTO;
import com.OEzoa.OEasy.application.community.DTO.CmnComment.UpdateCommentRequestDTO;
import com.OEzoa.OEasy.domain.community.OeBoard;
import com.OEzoa.OEasy.domain.community.OeBoardComment;
import com.OEzoa.OEasy.domain.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("/api/community/comment"))
@RequiredArgsConstructor
@Tag(name = "Community Comment API", description = "커뮤니티 댓글입니다.")
public class CommunityCommentController {

    private final CmnCommentService cmnCommentService;
    private final CmnValidator validator;

    @Operation(summary = "댓글 쓰기")
    @PostMapping
    public GetAllCommentResponseDTO createComment(@RequestBody CreateCommentRequestDTO dto){
        OeBoard board = validator.getBoard(dto.getCommunityId());
        Member member = validator.getMember(dto.getMemberId());
        validator.sizeValueCheck(dto.getSize());
        return cmnCommentService.createComment(member, board, dto.getContent(), dto.getSize());
    }

    @Operation(summary = "댓글 불러오기")
    @GetMapping
    public GetAllCommentResponseDTO getAllComment(@RequestParam Long communityPk,
                                                       @RequestParam int page,
                                                       @RequestParam int size) {
        OeBoard board = validator.getBoard(communityPk);
        return cmnCommentService.getAllComment(page, size, board);
    }

    @Operation(summary = "댓글 지우기")
    @DeleteMapping
    public void deleteComment(@RequestBody DeleteCommentRequestDTO dto){

        OeBoardComment comment = validator.myCommentCheck(dto.getMemberId(), dto.getCommentId());
        cmnCommentService.deleteComment(comment);
    }

    @Operation(summary = "댓글 수정")
    @PatchMapping
    public void updateComment(@RequestBody UpdateCommentRequestDTO dto){
        OeBoardComment comment = validator.myCommentCheck(dto.getMemberId(), dto.getCommentId());
        cmnCommentService.updateComment(comment, dto.getContent());
    }

}
