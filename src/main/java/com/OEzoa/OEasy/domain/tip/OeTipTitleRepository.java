package com.OEzoa.OEasy.domain.tip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OeTipTitleRepository extends JpaRepository<OeTipTitle, Long> {

    Optional<List<OeTipTitle>> findByOeTipOrderByOrderIndexAsc(OeTip oeTip);
}
