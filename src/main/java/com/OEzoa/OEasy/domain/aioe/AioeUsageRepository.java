package com.OEzoa.OEasy.domain.aioe;

import com.OEzoa.OEasy.domain.member.Member;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AioeUsageRepository extends JpaRepository<AioeUsage, AioeUsageId> {
    Optional<AioeUsage> findByMemberAndUsageDate(Member member, LocalDate usageDate);
}

