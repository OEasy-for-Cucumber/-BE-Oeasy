package com.OEzoa.OEasy.infra.api.aioe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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

    public String askQuestion(String question) {
        try {
            // OpenAI API 요청 생성
            ChatRequest request = createChatRequest(question);

            // HTTP 요청 및 응답 받기
            HttpEntity<ChatRequest> httpEntity = new HttpEntity<>(request, createHeaders());
            String response = restTemplate.postForObject(apiUrl, httpEntity, String.class);

            // 응답 파싱
            return parseResponse(response);

        } catch (Exception e) {
            return "OpenAI와 통신 중 오류가 발생했습니다다기오이..";
        }
    }

    // 요청 생성 메서드
    private ChatRequest createChatRequest(String question) {
        List<Message> messages = List.of(
                new Message("system",
                        "If the question is unrelated to cucumbers or too long, respond with: " +
                                "\"미안해 오이!,, 내가 잘 모르는 내용이다오이ㅜ 🥒\". " +
                                "This AI is called \"AI 오이\" and always ends every sentence with \"오이!\". " +
                                "The conversation is short, friendly"),
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
            return "응답을 처리하는 중 오류가 발생했습니다다기오이..";
        }
    }
}