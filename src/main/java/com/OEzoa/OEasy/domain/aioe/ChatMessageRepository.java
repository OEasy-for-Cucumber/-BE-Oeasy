package com.OEzoa.OEasy.domain.aioe;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByAiOeOrderByDateTimeAsc(AiOe aiOe); // 특정 챗봇에 연결된 메시지 조회
    void deleteByAiOe(AiOe aiOe);
}
