package com.OEzoa.OEasy.domain.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardImgRepository extends JpaRepository<OeBoardImg, Long> {
}
