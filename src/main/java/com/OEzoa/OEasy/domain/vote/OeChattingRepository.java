package com.OEzoa.OEasy.domain.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface OeChattingRepository extends JpaRepository<OeChatting, Long> {
}
