package com.OEzoa.OEasy.api.community;

import com.OEzoa.OEasy.application.community.*;
import com.OEzoa.OEasy.domain.community.OeBoard;
import com.OEzoa.OEasy.domain.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Community")
@Tag(name = "community API", description = "커뮤니티 게시판입니다.")
@RequiredArgsConstructor
public class CommunityController {

    private final CmnService cmnService;
    private final CmnValidator validator;

    @Operation(summary = "게시물 작성하기",
            description = "게시물을 작성")
    @PostMapping
    public ResponseEntity<String> createCmn(@RequestBody CmnCreateRequestDTO cmn) {
        Member member = validator.getMember(cmn.getUserId());
        cmnService.createCmn(cmn, member);
        return ResponseEntity.ok("성공!");
    }

//    @Operation(summary = "커뮤니티 게시판 불러오기",
//            description = "게시글들을 페이징 하여 불러옵니다.")
//    @GetMapping
//    public CmnDTOResponse getAllCmn(@PathVariable Long CmnId) {
//        return null;
//    }
    @Operation(summary = "커뮤니티 게시글 불러오기",
            description = "게시글을 불러옵니다.")
    @GetMapping("/{CmnId}")
    public ResponseEntity<CmnDTOResponse> getCmn(@PathVariable Long CmnId) {
        OeBoard board = validator.getBoard(CmnId);
        return ResponseEntity.ok(cmnService.getCmn(board));
    }

    @Operation(summary = "게시글 삭제", description = "본인의 게시글인 것을 확인 후 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<String> deleteCmn(@RequestBody CmnDeleteRequestDTO cmn) {
        validator.getMember(cmn.getUserId());
        System.out.println("cmn = " + cmn);
        OeBoard board = validator.getBoard(cmn.getCmnId());

        cmnService.deleteCmn(board);
        return ResponseEntity.ok("성공!");
    }

}
