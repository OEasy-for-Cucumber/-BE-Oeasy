package com.OEzoa.OEasy.util;

import org.springframework.stereotype.Component;

@Component
public class HeaderUtils {

    // Authorization 헤더에서 Bearer 토큰 추출
    public static String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("올바르지 않은 Authorization 헤더 형식입니다.");
        }
        return authorizationHeader.substring(7);
    }
}