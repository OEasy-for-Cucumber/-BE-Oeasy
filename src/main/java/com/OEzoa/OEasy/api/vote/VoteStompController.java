package com.OEzoa.OEasy.api.vote;

import com.OEzoa.OEasy.application.vote.*;
import com.OEzoa.OEasy.application.vote.DTO.ChattingResponseDTO;
import com.OEzoa.OEasy.application.vote.DTO.MessageRequestDTO;
import com.OEzoa.OEasy.application.vote.DTO.VoteInitResponseDTO;
import com.OEzoa.OEasy.application.vote.VoteService;
import com.OEzoa.OEasy.application.vote.DTO.VoteUserDTO;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.util.timeTrace.TimeTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@TimeTrace
@RequiredArgsConstructor
public class VoteStompController {

    private final VoteService voteService;
    private final VoteValidator voteValidator;


    @MessageMapping("/message") // 클라이언트에서 "/app/send"로 보내는 메시지를 처리
    @SendTo("/topic/message") // 모든 구독자에게 "/topic/message"로 메시지 전송
    public ChattingResponseDTO sendMessage(MessageRequestDTO message) {
        Member member = voteValidator.getMember(message.getUserPk());

        return voteService.sendMessage(member, message.getContent());
    }

    @MessageMapping("/votes")
    @SendTo("/topic/votes")
    public VoteInitResponseDTO likeVotes(VoteUserDTO voteUserDTO) {
        Member member = voteValidator.getMember(voteUserDTO.getId());
        return voteService.voting(member,voteUserDTO.isVote());
    }



}
