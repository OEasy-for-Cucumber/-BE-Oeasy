package com.OEzoa.OEasy.domain.aioe;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByAiOeOrderByDateTimeAsc(AiOe aiOe); // 특정 챗봇에 연결된 메시지 조회
    void deleteByAiOe(AiOe aiOe);
    // 기존에 연결된 챗봇이 있나 탐색
    ChatMessage findFirstByAiOeAndTypeOrderByDateTimeAsc(AiOe aiOe, String aioe);
}
