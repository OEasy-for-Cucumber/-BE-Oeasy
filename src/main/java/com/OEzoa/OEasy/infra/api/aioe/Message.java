package com.OEzoa.OEasy.infra.api.aioe;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String role;    // "system", "user", or "assistant"
    private String content; // 메시지 내용
}
