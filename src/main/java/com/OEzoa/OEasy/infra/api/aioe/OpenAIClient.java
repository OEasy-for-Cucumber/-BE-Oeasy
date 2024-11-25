package com.OEzoa.OEasy.infra.api.aioe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAIClient {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    // 캐시 저장소 (질문 정규화된 키 -> 응답)
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public String askQuestion(String question) {
        try {
            // 질문 정규화
            String normalizedQuestion = normalizeQuestion(question);

            // 캐시 확인
            if (cache.containsKey(normalizedQuestion)) {
                log.info("캐시에서 결과 반환: {}", normalizedQuestion);
                return cache.get(normalizedQuestion);
            }

            // OpenAI API 요청 생성
            ChatRequest request = createChatRequest(question);

            // HTTP 요청 및 응답 받기
            HttpEntity<ChatRequest> httpEntity = new HttpEntity<>(request, createHeaders());
            String response = restTemplate.postForObject(apiUrl, httpEntity, String.class);

            // 응답 파싱
            String parsedResponse = parseResponse(response);

            // 캐시에 저장
            cache.put(normalizedQuestion, parsedResponse);

            return parsedResponse;

        } catch (Exception e) {
            log.error("OpenAI 통신 중 오류 발생: {}", e.getMessage(), e);
            return "OpenAI와 통신 중 오류가 발생했습니다다기오이..🥒";
        }
    }

    // 질문 정규화 메서드
    private String normalizeQuestion(String question) {
        if (question == null) {
            return "";
        }
        // 공백 제거, 소문자 변환, 특수 문자 제거
        return question.replaceAll("\\s+", "").toLowerCase().replaceAll("[^a-z가-힣0-9]", "");
    }

    // 요청 생성 메서드
    private ChatRequest createChatRequest(String question) {
        List<Message> messages = List.of(
                new Message("system",
                        "This is 'AI 오이'. Only answer cucumber-related questions, ending each sentence with '오이!🥒'"),
                new Message("user", question)
        );
        return new ChatRequest("gpt-4o-mini", messages, 100, 0.3, 1.0);
    }

    // HTTP 헤더 생성 메서드
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        return headers;
    }

    // 응답 JSON 파싱 메서드
    private String parseResponse(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            log.error("응답 파싱 중 오류 발생: {}", e.getMessage(), e);
            return "응답을 처리하는 중 오류가 발생했습니다다기오이..";
        }
    }
}
