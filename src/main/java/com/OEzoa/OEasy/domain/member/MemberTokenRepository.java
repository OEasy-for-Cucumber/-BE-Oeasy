package com.OEzoa.OEasy.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {

}

