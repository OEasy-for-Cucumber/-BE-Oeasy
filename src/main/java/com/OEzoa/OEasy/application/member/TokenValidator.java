package com.OEzoa.OEasy.application.member;

import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.domain.member.MemberTokenRepository;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.util.member.JwtTokenProvider;
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

    public Member validateAccessTokenAndReturnMember(String accessToken)   {
        log.info("Access token 유효성 검사 시작: {}", accessToken); // 토큰 유효성 검사 로그

        if (accessToken == null) {
            throw new GlobalException(GlobalExceptionCode.INVALID_ACCESS_TOKEN);
        }

        try {
            // JWT 토큰에서 사용자 ID 추출
            Long memberId = jwtTokenProvider.getMemberIdFromToken(accessToken);
            log.info("토큰이 유효합니다. Member ID: {}", memberId);

            // 사용자 ID로 회원 정보 조회
            return memberRepository.findById(memberId)
                    .orElseThrow(() -> new GlobalException(GlobalExceptionCode.MEMBER_NOT_FOUND));
        } catch (ExpiredJwtException e) {
            log.error("토큰이 만료되었습니다.", e);
            throw new GlobalException(GlobalExceptionCode.EXPIRED_ACCESS_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 JWT 토큰입니다.", e);
            throw new GlobalException(GlobalExceptionCode.UNSUPPORTED_TOKEN);
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 토큰 형식입니다.", e);
            throw new GlobalException(GlobalExceptionCode.MALFORMED_TOKEN);
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다.", e);
            throw new GlobalException(GlobalExceptionCode.INVALID_ACCESS_TOKEN);
        }
    }
}
