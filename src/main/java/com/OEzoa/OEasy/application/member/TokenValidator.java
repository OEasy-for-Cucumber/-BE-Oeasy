package com.OEzoa.OEasy.application.member;

import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.domain.member.MemberTokenRepository;
import com.OEzoa.OEasy.util.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenValidator {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;

    public TokenValidator(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository,
                          MemberTokenRepository memberTokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.memberTokenRepository = memberTokenRepository;
    }

    public Member validateAccessTokenAndReturnMember(String accessToken) throws Exception {
        log.info("Access token 유효성 검사 시작: {}", accessToken); // 토큰 유효성 검사 로그

        if (accessToken == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다. 액세스 토큰이 존재하지 않습니다.");
        }

        try {
            // JWT 토큰에서 사용자 ID를 추출
            Long memberId = jwtTokenProvider.getMemberIdFromToken(accessToken);
            log.info("토큰이 유효합니다. Member ID: {}", memberId); // 유효한 토큰의 사용자 ID 로그

            // 사용자 ID를 통해 데이터베이스에서 회원 정보를 조회
            return memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. 잘못된 사용자 ID입니다."));
        } catch (ExpiredJwtException e) {
            log.error("토큰이 만료되었습니다.", e); // 토큰 만료 예외 로그
            throw new ExpiredJwtException(null, null, "액세스 토큰이 만료되었습니다. 리프레시 토큰을 포함하여 다시 요청하세요.");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 JWT 토큰입니다.", e); // 지원하지 않는 토큰 형식 예외 로그
            throw new IllegalArgumentException("지원하지 않는 JWT 토큰입니다.", e);
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 토큰 형식입니다.", e); // 잘못된 토큰 형식 예외 로그
            throw new IllegalArgumentException("잘못된 JWT 토큰 형식입니다.", e);
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다.", e); // 그 외의 토큰 유효성 오류 로그
            throw new IllegalStateException("유효하지 않은 토큰입니다.", e);
        }
    }

}
