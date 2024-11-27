package com.OEzoa.OEasy.api.community;

import com.OEzoa.OEasy.application.community.*;
import com.OEzoa.OEasy.application.community.DTO.Cmn.*;
import com.OEzoa.OEasy.domain.community.OeBoard;
import com.OEzoa.OEasy.domain.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/community")
@Tag(name = "Community API", description = "커뮤니티 게시판입니다.")
@RequiredArgsConstructor
public class CommunityController {

    private final CmnService cmnService;
    private final CmnValidator validator;

    @Operation(summary = "게시물 작성하기",
            description = "게시물을 작성")
    @PostMapping
    public ResponseEntity<String> createCmn(@RequestBody CmnCreateRequestDTO cmn) {
        System.out.println("cmn.getImgList().get(0).getName() = " + cmn.getImgList().size());
        System.out.println("cmn.getImgList().get(0).getName() = " + cmn.getImgList().get(0).getName());
        Member member = validator.getMember(cmn.getUserId());
        cmnService.createCmn(cmn, member);
        return ResponseEntity.ok("성공!");
    }


    @Operation(summary = "게시물 수정하기",
            description = "게시물을 수정")
    @PatchMapping
    public ResponseEntity<String> updateCmn(@RequestBody CmnUpdateRequestDTO cmn) {
        OeBoard board = validator.myBoardCheck(cmn.getUserId(), cmn.getCommunityId());
        cmnService.updateCmn(board, cmn);
        return ResponseEntity.ok("성공!");
    }


    @Operation(summary = "커뮤니티 게시판 불러오기",
            description = "게시글들을 페이징 하여 불러옵니다.")
    @GetMapping
    public Page<CmnBoardListResponseDTO> getAllCmn(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String sortKeyword,
            @RequestParam(required = false) Boolean sortType
    ) {
        CmnBoardListRequestDTO dto = new CmnBoardListRequestDTO(page, size, searchKeyword, searchType, sortKeyword, sortType);
        return cmnService.searchBoard(dto);
    }


    @Operation(summary = "커뮤니티 게시글 불러오기",
            description = "게시글을 불러옵니다.")
    @GetMapping("{cmnId}/{memberId}")
    public ResponseEntity<CmnDTOResponse> getCmn(@PathVariable Long cmnId,
                                                 @PathVariable Long memberId) {
        OeBoard board = validator.getBoard(cmnId);
        Member member = validator.getMember(memberId);
        return ResponseEntity.ok(cmnService.getCmn(board, member));
    }

    @Operation(summary = "게시글 삭제",
            description = "본인의 게시글인 것을 확인 후 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<String> deleteCmn(@RequestBody CmnDeleteRequestDTO cmn) {
        validator.getMember(cmn.getUserId());
        OeBoard board = validator.getBoard(cmn.getCmnId());

        cmnService.deleteCmn(board);
        return ResponseEntity.ok("성공!");
    }

    @Operation(summary = "좋아요",
        description = "좋아요 혹은 좋아요 취소를 합니다.")
    @GetMapping("/like/{cmnId}/{memberId}")
    public Boolean likeCmn(@PathVariable Long memberId,
                           @PathVariable Long cmnId) {
        Member member = validator.getMember(memberId);
        OeBoard board = validator.getBoard(cmnId);
        return cmnService.cmnLike(member, board);
    }

    @Operation(summary = "뷰 올리기")
    @GetMapping("/view/{communityId}")
    public void plusView(@PathVariable Long communityId){
        OeBoard board = validator.getBoard(communityId);
        cmnService.plusView(board);
    }

//    @GetMapping("/test")
//    public void test() {
//        Member member = validator.getMember(38L);
//
//
//        for(int i = 0; i< 10000; i++) {
//            CmnCreateRequestDTO cmn = CmnCreateRequestDTO.builder()
//                    .title("제목"+i)
//                    .content("내용"+(10000-i))
//                    .imgList(new ArrayList<>())
//                    .build();
//            cmnService.createCmn(cmn, member);
//        }
//    }


}
