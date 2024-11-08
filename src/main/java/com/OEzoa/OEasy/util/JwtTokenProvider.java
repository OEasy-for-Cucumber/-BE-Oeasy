package com.OEzoa.OEasy.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    private final long TOKEN_VALIDITY = 1000L * 60 * 60;  // 1시간 유효
    private final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7;  // 7일 유효

    // JWT 토큰 생성 메서드
    public String generateToken(Long userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALIDITY);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // JWT Refresh 토큰 생성 메서드
    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 토큰에서 사용자 ID 추출 메서드
    // 토큰을 검증하고, 페이로드에서 sub 필드값을 추출 -> Long으로 변경-> 사용자 ID!! 더 공부하자
    public Long getMemberIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // 검증하기 위해 시크릿 키 설정
                .build() // 호출로 파서 생성
                .parseClaimsJws(token) // 토큰 파싱 후 클레임데이터 추출 ( 페이로드 )
                .getBody(); // 페이로드 반환

        return Long.parseLong(claims.getSubject()); // sub 필드의 값 추출
    }
}
