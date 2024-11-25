package com.OEzoa.OEasy.domain.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface OeChattingRepository extends JpaRepository<OeChatting, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM oe_chatting ORDER BY chatting_pk DESC LIMIT 50")
    List<OeChatting> findAllByOrderByIdDescLimit50();

}
