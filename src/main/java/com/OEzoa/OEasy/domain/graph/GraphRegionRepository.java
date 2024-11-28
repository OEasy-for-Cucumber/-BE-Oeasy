package com.OEzoa.OEasy.domain.graph;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphRegionRepository extends JpaRepository<OeGraphRegion, Long> {
    boolean existsByDateAndRegion(String date, String region); // 중복 데이터 확인
    List<OeGraphRegion> findAllByDate(String date);

    @Transactional
    @Modifying
    int deleteByDate(String date);

    List<OeGraphRegion> findAll();
}
