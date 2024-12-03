package com.OEzoa.OEasy.api.vote;


import com.OEzoa.OEasy.application.vote.DTO.VoteInitResponseDTO;
import com.OEzoa.OEasy.application.vote.DTO.VoteStatusRequestDTO;
import com.OEzoa.OEasy.application.vote.VoteService;
import com.OEzoa.OEasy.application.vote.VoteValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OE Vote API", description = "투표 관련 정보들을 제공합니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/community/")
public class VoteController {

    private final VoteService voteService;
    private final VoteValidator voteValidator;

    @Operation(summary = "로그인 정보가 있을 때")
    @GetMapping("/init/{id}")
    public ResponseEntity<VoteInitResponseDTO> init(@PathVariable long id) {
        return ResponseEntity.ok(voteService.init(voteValidator.getMember(id)));
    }

    @Operation(summary = "로그인 정보가 없을 때")
    @GetMapping("/init")
    public ResponseEntity<VoteInitResponseDTO> init() {
        return ResponseEntity.ok(voteService.init());
    }

    @Operation(summary = "투표 찬반 정보를 불러옵니다.")
    @GetMapping("/vote-status")
    public ResponseEntity<VoteStatusRequestDTO> getVoteStatus(){
        return ResponseEntity.ok(voteService.getVoteStatus());
    }


}
