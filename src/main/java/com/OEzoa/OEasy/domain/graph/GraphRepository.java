package com.OEzoa.OEasy.domain.graph;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphRepository extends JpaRepository<OeGraph, String> {
    List<OeGraph> findByDateBetween(String startDate, String endDate);
    // 중복 검증용
    boolean existsByDate(String date);
}