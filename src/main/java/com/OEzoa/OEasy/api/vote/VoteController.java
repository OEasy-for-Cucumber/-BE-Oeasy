package com.OEzoa.OEasy.api.vote;


import com.OEzoa.OEasy.application.vote.DTO.VoteInitResponseDTO;
import com.OEzoa.OEasy.application.vote.VoteService;
import com.OEzoa.OEasy.application.vote.VoteValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/community/")
public class VoteController {

    private final VoteService voteService;
    private final VoteValidator voteValidator;

    @GetMapping("/init/{id}")
    public ResponseEntity<VoteInitResponseDTO> init(@PathVariable long id) {
        return ResponseEntity.ok(voteService.init(voteValidator.getMember(id)));
    }


}
