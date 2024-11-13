package com.OEzoa.OEasy.api.member;

import com.OEzoa.OEasy.domain.member.MemberToken;
import com.OEzoa.OEasy.domain.member.MemberTokenRepository;
import com.OEzoa.OEasy.util.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberTokenRepository memberTokenRepository;

    public AuthController(JwtTokenProvider jwtTokenProvider, MemberTokenRepository memberTokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberTokenRepository = memberTokenRepository;
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "리프레시 토큰을 통한 액세스 토큰 재발급",
            description = "만료된 액세스 토큰을 리프레시 토큰으로 갱신합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "액세스 토큰과 리프레시 토큰 재발급 성공."),
                    @ApiResponse(responseCode = "401", description = "리프레시 토큰 만료로 인한 재발급 실패."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 토큰 갱신 중 오류 발생.")
            }
    )
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String refreshTokenHeader) {
        try {
            logger.info("리프레시 토큰을 통해 액세스 토큰 갱신 시도. 리프레시 토큰: {}", refreshTokenHeader);

            // "Bearer " 접두사 제거
            String refreshToken = refreshTokenHeader.replace("Bearer ", "").trim();

            // 리프레시 토큰 검증
            if (jwtTokenProvider.isRefreshTokenExpired(refreshToken)) {
                logger.warn("리프레시 토큰이 만료되었습니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("리프레시 토큰이 만료되었습니다. 다시 로그인 해주세요.");
            }

            // 리프레시 토큰이 유효한 경우 사용자 ID 추출
            Long memberId = jwtTokenProvider.getMemberIdFromToken(refreshToken);
            logger.info("리프레시 토큰이 유효합니다. Member ID: {}", memberId);

            // 새로운 액세스 토큰 생성
            String newAccessToken = jwtTokenProvider.generateToken(memberId);
            logger.info("새로운 액세스 토큰 발급 성공: {}", newAccessToken);

            // 필요한 경우 새로운 리프레시 토큰도 발급
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(memberId);
            logger.info("새로운 리프레시 토큰 발급 성공: {}", newRefreshToken);

            // 새로 발급한 리프레시 토큰을 DB에 저장
            MemberToken memberToken = memberTokenRepository.findByMemberPk(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
            memberToken = memberToken.toBuilder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
            memberTokenRepository.save(memberToken);
            logger.info("새로운 토큰 정보가 데이터베이스에 저장되었습니다.");

            // 새 토큰 반환
            return ResponseEntity.ok(Map.of(
                    "accessToken", newAccessToken,
                    "refreshToken", newRefreshToken
            ));

        } catch (IllegalArgumentException e) {
            logger.error("회원 정보를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            logger.error("토큰 갱신 중 오류가 발생했습니다.", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰 갱신 중 오류가 발생했습니다.");
        }
    }
}
