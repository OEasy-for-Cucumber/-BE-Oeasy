package com.OEzoa.OEasy.application.vote;

import com.OEzoa.OEasy.application.vote.DTO.ChattingResponseDTO;
import com.OEzoa.OEasy.application.vote.DTO.VoteInitResponseDTO;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.vote.OeChatting;
import com.OEzoa.OEasy.domain.vote.OeChattingRepository;
import com.OEzoa.OEasy.domain.vote.OeVote;
import com.OEzoa.OEasy.domain.vote.OeVoteRepository;
import com.OEzoa.OEasy.util.timeTrace.TimeTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@TimeTrace
@Slf4j
public class VoteService {
    private final OeChattingRepository oeChattingRepository;
    private final OeVoteRepository oeVoteRepository;


    //----------------------------Stomp Start---------------------------------------
    /*
     input 메시지, 유저pk, 시간

     */
    public ChattingResponseDTO sendMessage(Member member, String message){
        OeChatting oeChatting = OeChatting.builder()
                .member(member)
                .content(message)
                .chattingTimestamp(LocalDateTime.now())
                .build();
        oeChatting = oeChattingRepository.save(oeChatting);

        return OeChatting.of(member, oeChatting.getContent());
    }

    public long voting(Member member, boolean hateAndLike){
        //hateAndLike true : 좋아요, false : 싫어요
        Optional<OeVote> oVote = oeVoteRepository.findByMemberAndDate(member, LocalDate.now());
        log.info("java time is {}", LocalDateTime.now());
        OeVote vote;
        if(oVote.isPresent()){
            vote= oVote.get();
            if(vote.getVote() == hateAndLike){//같은 날 같은 곳에 투표했을 떄
                oeVoteRepository.delete(vote);
            }else{ // 바꾼 결과로 수정
                oeVoteRepository.save(vote.toBuilder().vote(hateAndLike).build());
            }
        }else{
            vote = OeVote.builder().member(member)
                    .vote(hateAndLike)
                    .date(LocalDate.now()).build();
            vote = oeVoteRepository.save(vote);
            log.info("mySql time is {}", vote.getDate());
        }

        return oeVoteRepository.countByVote(hateAndLike);
    }

    //----------------------------Stomp end---------------------------------------
    public VoteInitResponseDTO init(){
        return VoteInitResponseDTO.builder()
                .hate(oeVoteRepository.countByVote(false))
                .like(oeVoteRepository.countByVote(true))
                .build();
    }
}
