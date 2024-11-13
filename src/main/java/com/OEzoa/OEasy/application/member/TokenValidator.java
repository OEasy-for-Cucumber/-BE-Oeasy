package com.OEzoa.OEasy.application.member;

import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.domain.member.MemberToken;
import com.OEzoa.OEasy.domain.member.MemberTokenRepository;
import com.OEzoa.OEasy.util.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.stereotype.Component;

@Component
public class TokenValidator {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;

    public TokenValidator(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository, MemberTokenRepository memberTokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.memberTokenRepository = memberTokenRepository;
    }

    // 액세스 토큰 검증 및 Member 객체 조회 메서드
    public Member validateAccessTokenAndReturnMember(String accessToken) throws Exception {
        if (accessToken == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다. 액세스 토큰이 존재하지 않습니다.");
        }

        try {
            // JWT 토큰에서 사용자 ID 추출
            Long memberId = jwtTokenProvider.getMemberIdFromToken(accessToken);
            return memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. 잘못된 사용자 ID입니다."));
        } catch (ExpiredJwtException e) {
            // 토큰 만료 시 새로운 토큰 발급 로직 추가
            String refreshToken = getRefreshTokenForMember(accessToken);
            if (refreshToken == null) {
                throw new IllegalStateException("토큰이 만료되었습니다. 다시 로그인 해 주세요.", e);
            }
            Long memberId = jwtTokenProvider.getMemberIdFromToken(refreshToken);
            String newAccessToken = jwtTokenProvider.generateToken(memberId);
            return memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. 잘못된 사용자 ID입니다."));
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("지원하지 않는 JWT 토큰입니다.", e);
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException("잘못된 JWT 토큰 형식입니다.", e);
        } catch (Exception e) {
            throw new IllegalStateException("유효하지 않은 토큰입니다.", e);
        }
    }

    // 토큰 갱신 메서드 추가
    private String getRefreshTokenForMember(String accessToken) {
        // 토큰에서 사용자 ID 추출
        Long memberId = jwtTokenProvider.getMemberIdFromToken(accessToken);
        MemberToken memberToken = memberTokenRepository.findByMemberPk(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. 잘못된 사용자 ID입니다."));

        if (jwtTokenProvider.isTokenExpired(memberToken.getRefreshToken())) {
            throw new IllegalStateException("리프레시 토큰이 만료되었습니다. 다시 로그인 해 주세요.");
        }
        return memberToken.getRefreshToken();
    }
}
