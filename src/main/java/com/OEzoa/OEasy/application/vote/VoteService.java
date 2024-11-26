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
import java.util.ArrayList;
import java.util.List;
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

        return ChattingResponseDTO.of(oeChatting);
    }

    public VoteInitResponseDTO voting(Member member, boolean hateAndLike){
        //hateAndLike true : 좋아요, false : 싫어요
        LocalDate today = LocalDate.now();
        Optional<OeVote> oVote = oeVoteRepository.findByMemberAndDate(member, today);
        String isVoting;
        log.info("java time is {}", today);
        OeVote vote;
        if(oVote.isPresent()){
            vote= oVote.get();
            if(vote.getVote() == hateAndLike){//같은 날 같은 곳에 투표했을 떄
                oeVoteRepository.delete(vote);
                isVoting = "not voting";
            }else{ // 바꾼 결과로 수정
                oeVoteRepository.save(vote.toBuilder().vote(hateAndLike).build());
                isVoting = hateAndLike ? "like" : "hate";
            }
        }else{
            vote = OeVote.builder().member(member)
                    .vote(hateAndLike)
                    .date(today).build();
            vote = oeVoteRepository.save(vote);

            log.info("mySql time is {}", vote.getDate());
            isVoting = hateAndLike ? "like" : "hate";
        }

        return VoteInitResponseDTO.builder()
                .hate(oeVoteRepository.countByVote(false))
                .like(oeVoteRepository.countByVote(true))
                .isVoting(isVoting)
                .build();
    }

    //----------------------------Stomp end---------------------------------------
    public VoteInitResponseDTO init(Member member){
        String isVoting;
        Optional<OeVote> oVote = oeVoteRepository.findByMemberAndDate(member, LocalDate.now());
        if(oVote.isPresent()){
            isVoting = oVote.get().getVote() ? "like" : "hate";
        }else isVoting = "not voting";
        List<OeChatting> OeChattingList= oeChattingRepository.findAllByOrderByIdDescLimit50();;
        List<ChattingResponseDTO> dtoList = new ArrayList<>();

        for (OeChatting oeChatting : OeChattingList) {
            dtoList.add(ChattingResponseDTO.of(oeChatting));
        }

        return VoteInitResponseDTO.builder()
                .hate(oeVoteRepository.countByVote(false))
                .like(oeVoteRepository.countByVote(true))
                .isVoting(isVoting)
                .chattingList(dtoList)
                .build();
    }
}
