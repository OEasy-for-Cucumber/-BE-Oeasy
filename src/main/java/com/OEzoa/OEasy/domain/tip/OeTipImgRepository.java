package com.OEzoa.OEasy.domain.tip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OeTipImgRepository extends JpaRepository<OeTipImg, Long> {
    @Query(value = "SELECT * FROM oe_tip_img ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<OeTipImg> findRandomOeTip();
}
