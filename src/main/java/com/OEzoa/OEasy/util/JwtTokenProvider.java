package com.OEzoa.OEasy.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKeyPlain;  // 설정 파일로부터 읽어올 비밀 키 (평문 상태)

    private final long TOKEN_VALIDITY = 1000L * 60 * 5;  // 액세스 토큰 유효 시간: 10분
    private final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7;  // 리프레시 토큰 유효 시간: 7일
    private SecretKey secretKey;  // 실제 서명 및 검증에 사용할 인코딩된 비밀 키

    @PostConstruct
    protected void init() {
        // Base64 인코딩된 secretKeyPlain을 디코딩하여 SecretKey 생성
        System.out.println("플레인 시크릿 키 (Base64 인코딩된 값): " + secretKeyPlain);
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyPlain);
        secretKey = Keys.hmacShaKeyFor(decodedKey);  // Base64 디코딩된 바이트 배열로 SecretKey 생성
        System.out.println("시크릿 키 (디코딩 후 SecretKey 객체): " + secretKey);
    }

    // JWT 액세스 토큰 생성 메서드
    public String generateToken(Long userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALIDITY);  // 유효 시간 설정

        return Jwts.builder()
                .setSubject(String.valueOf(userId))  // 토큰의 사용자 식별자 설정
                .setIssuedAt(now)  // 발행 시간 설정
                .setExpiration(validity)  // 만료 시간 설정
                .signWith(secretKey, SignatureAlgorithm.HS256)  // 서명에 SecretKey 사용
                .compact();  // 토큰 생성
    }

    // JWT 리프레시 토큰 생성 메서드
    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY);  // 리프레시 토큰 유효 시간 설정

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)  // 서명에 SecretKey 사용
                .compact();
    }

    // 토큰에서 사용자 ID 추출 메서드
    public Long getMemberIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)  // 검증에 사용할 SecretKey
                .build()
                .parseClaimsJws(token)
                .getBody();  // 토큰의 본문(Claims) 추출

        return Long.parseLong(claims.getSubject());  // 사용자 ID 추출
    }

    // 토큰 만료 여부 확인 메서드
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)  // 만료 검증에 사용할 SecretKey
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());  // 만료 시간 확인
        } catch (ExpiredJwtException e) {
            return true;  // 만료된 경우 예외 발생
        }
    }
    // 리프레시 토큰이 유효한지 확인하는 메서드
    public boolean isRefreshTokenExpired(String refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}
