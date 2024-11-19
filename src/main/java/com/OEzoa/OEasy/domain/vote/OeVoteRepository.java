package com.OEzoa.OEasy.domain.vote;

import com.OEzoa.OEasy.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OeVoteRepository extends JpaRepository<OeVote,Long> {

    Optional<OeVote> findByMemberAndDate(Member member, LocalDate date);

    long countByVote(boolean vote);
}
