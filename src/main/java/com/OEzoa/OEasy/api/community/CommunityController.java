package com.OEzoa.OEasy.api.community;

import com.OEzoa.OEasy.application.community.*;
import com.OEzoa.OEasy.application.community.DTO.Cmn.*;
import com.OEzoa.OEasy.domain.community.OeBoard;
import com.OEzoa.OEasy.domain.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyEditorSupport;

@RestController
@RequestMapping("/api/community")
@Tag(name = "Community API", description = "커뮤니티 게시판입니다.")
@RequiredArgsConstructor
@Slf4j
public class CommunityController {

    private final CmnService cmnService;
    private final CmnValidator validator;

    @Operation(summary = "게시물 작성하기",
            description = "게시물을 작성")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<CmnBoardListResponseDTO> createCmn(@ModelAttribute CmnCreateRequestDTO cmn) {
        Member member = validator.getMember(cmn.getUserId());
        cmnService.createCmn(cmn, member);
        CmnBoardListRequestDTO dto = new CmnBoardListRequestDTO(0, 15, "", "titleAndContent", "boardPk", false);
        return ResponseEntity.ok(cmnService.searchBoard(dto));
    }


    @Operation(summary = "게시물 수정하기",
            description = "게시물을 수정")
    @PatchMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<CmnDTOResponseDTO> updateCmn(@ModelAttribute @Valid CmnUpdateRequestDTO cmn) {
        OeBoard board = validator.myBoardCheck(cmn.getUserId(), cmn.getCommunityId());
        Member member = validator.getMember(cmn.getUserId());
        cmnService.updateCmn(board, cmn);
        return ResponseEntity.ok(cmnService.getCmn(board, member));
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(MultipartFile.class, new PropertyEditorSupport() {

            @Override
            public void setAsText(String text) {
                log.debug("initBinder MultipartFile.class: {}; set null;", text);
                setValue(null);
            }

        });
    }

    @Operation(summary = "커뮤니티 게시판 불러오기",
            description = "게시글들을 페이징 하여 불러옵니다.")
    @GetMapping
    public ResponseEntity<CmnBoardListResponseDTO> getAllCmn(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String sortKeyword,
            @RequestParam(required = false) Boolean sortType
    ) {
        if(searchKeyword == null) searchKeyword = "";
        CmnBoardListRequestDTO dto = new CmnBoardListRequestDTO(page, size, searchKeyword, searchType, sortKeyword, sortType);
        validator.pageCheck(dto);
        return ResponseEntity.ok(cmnService.searchBoard(dto));
    }

    @Operation(summary = "좋아요를 누른 게시판 불러오기",
            description = "게시글들을 페이징 하여 불러옵니다.")
    @GetMapping("/my-likes")
    public ResponseEntity<CmnBoardListResponseDTO> getAllMyLikes(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size,
            @RequestParam(required = false) int memberId
    ) {
        Member member = validator.getMember(memberId);
        return ResponseEntity.ok(cmnService.getAllLikesCmn(member, page, size));
    }

    @Operation(summary = "내가 작성한 게시판 불러오기",
            description = "게시글들을 페이징 하여 불러옵니다.")
    @GetMapping("/my-Cmn")
    public ResponseEntity<CmnBoardListResponseDTO> getAllMyCmn(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size,
            @RequestParam(required = false) int memberId
    ) {
        Member member = validator.getMember(memberId);
        return ResponseEntity.ok(cmnService.getAllMyCmn(member, page, size));
    }

    @Operation(summary = "커뮤니티 게시글 불러오기",
            description = "게시글을 불러옵니다.")
    @GetMapping("{cmnId}/{memberId}")
    public ResponseEntity<CmnDTOResponseDTO> getCmn(@PathVariable Long cmnId,
                                                    @PathVariable Long memberId) {
        OeBoard board = validator.getBoard(cmnId);
        Member member = validator.getMember(memberId);
        return ResponseEntity.ok(cmnService.getCmn(board, member));
    }

    @Operation(summary = "게시글 삭제",
            description = "본인의 게시글인 것을 확인 후 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<CmnBoardListResponseDTO> deleteCmn(@RequestBody CmnDeleteRequestDTO cmn) {
        validator.getMember(cmn.getUserId());
        OeBoard board = validator.getBoard(cmn.getCmnId());

        cmnService.deleteCmn(board);
        CmnBoardListRequestDTO dto = new CmnBoardListRequestDTO(0, 15, "", "titleAndContent", "boardPk", false);
        return ResponseEntity.ok(cmnService.searchBoard(dto));
    }

    @Operation(summary = "좋아요",
        description = "좋아요 혹은 좋아요 취소를 합니다.")
    @GetMapping("/like/{cmnId}/{memberId}")
    public ResponseEntity<Boolean> likeCmn(@PathVariable Long memberId,
                           @PathVariable Long cmnId) {
        Member member = validator.getMember(memberId);
        OeBoard board = validator.getBoard(cmnId);
        return ResponseEntity.ok(cmnService.cmnLike(member, board));
    }

    @Operation(summary = "뷰 올리기")
    @GetMapping("/view/{communityId}")
    public void plusView(@PathVariable Long communityId){
        OeBoard board = validator.getBoard(communityId);
        cmnService.plusView(communityId);
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
