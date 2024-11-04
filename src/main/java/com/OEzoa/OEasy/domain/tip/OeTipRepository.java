package com.OEzoa.OEasy.domain.tip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OeTipRepository extends JpaRepository<OeTip, Long> {
    @Query(value = "SELECT * FROM oe_tip ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<OeTip> findRandomOeTip();
}
