package com.OEzoa.OEasy.domain.aioe;

import com.OEzoa.OEasy.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AioeRepository extends JpaRepository<AiOe, Long> {
    // 사용자 챗봇 가져오는 메서드
    Optional<AiOe> findByMember(Member member);
}
