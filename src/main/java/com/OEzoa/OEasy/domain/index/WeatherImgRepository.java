package com.OEzoa.OEasy.domain.index;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherImgRepository extends JpaRepository<WeatherImg, Integer> {
}
