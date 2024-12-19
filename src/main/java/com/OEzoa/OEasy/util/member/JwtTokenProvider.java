package com.OEzoa.OEasy.util.member;

import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKeyPlain;  // 설정 파일로부터 읽어올 비밀 키 (평문 상태)

    private final long TOKEN_VALIDITY = 1000L * 60 * 1;  // 액세스 토큰 유효 시간: 2시간
    private final long LOGIN_TOKEN_VALIDITY = 1000L * 60 * 10;  // 회원가입 토큰 유효 시간: 10분
    private final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 3;  // 리프레시 토큰 유효 시간: 7일
    private SecretKey secretKey;  // 실제 서명 및 검증에 사용할 인코딩된 비밀 키

    @PostConstruct
    protected void init() {
        // Base64 인코딩된 secretKeyPlain을 디코딩하여 SecretKey 생성
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyPlain);
        secretKey = Keys.hmacShaKeyFor(decodedKey);  // Base64 디코딩된 바이트 배열로 SecretKey 생성
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

    public String generateTokenWithData(Map<String, Object> data) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + LOGIN_TOKEN_VALIDITY);
        log.info("JWT 생성 - 입력 데이터: {}", data);

        // JWT 생성
        String token = Jwts.builder()
                .setClaims(data) // 데이터를 클레임으로 저장
                .setIssuedAt(now) // 토큰 발행 시간
                .setExpiration(validity) // 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS256) // 서명
                .compact();
        log.info("JWT 생성 완료: {}", token);
        return token;
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

    // JWT 데이터 추출
    public Map<String, Object> extractDataFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    // Refresh Token을 HttpOnly 쿠키로 설정
    public void createRefreshTokenCookie(Long userId, HttpServletResponse response) {
        String refreshToken = generateRefreshToken(userId);
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(REFRESH_TOKEN_VALIDITY)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        log.info("Refresh Token 쿠키 생성 완료: {}", refreshCookie);
    }

    public void validateRefreshToken(String refreshToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(refreshToken);
        } catch (ExpiredJwtException e) {
            log.warn("Refresh Token 만료: {}", e.getMessage());
            throw new GlobalException(GlobalExceptionCode.REFRESH_TOKEN_EXPIRED); // 401 예외
        } catch (Exception e) {
            log.error("Refresh Token 검증 실패: {}", e.getMessage());
            throw new GlobalException(GlobalExceptionCode.INVALID_REFRESH_TOKEN); // 400 예외
        }
    }
}
