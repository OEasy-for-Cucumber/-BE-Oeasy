package com.OEzoa.OEasy.infra.api.aioe;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRequest {
    private String model; // 모델 이름
    private List<Message> messages; // 메시지 리스트
    private Integer max_tokens; // 응답 최대 토큰
    private Double temperature; // 창의성
    private Double top_p; // 확률 분포
}
