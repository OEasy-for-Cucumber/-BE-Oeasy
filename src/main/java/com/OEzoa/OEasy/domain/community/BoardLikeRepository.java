package com.OEzoa.OEasy.domain.community;

import com.OEzoa.OEasy.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardLikeRepository extends JpaRepository<OeBoardLike, Long> {
    Long countByBoard(OeBoard board);
    Long countByMember(Member member);
}
