package com.OEzoa.OEasy.application.member;

import com.OEzoa.OEasy.application.member.dto.AuthTokenResponseDTO;
import com.OEzoa.OEasy.application.member.mapper.MemberTokenMapper;
import com.OEzoa.OEasy.domain.member.MemberToken;
import com.OEzoa.OEasy.domain.member.MemberTokenRepository;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.util.member.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberTokenRepository memberTokenRepository;
    private final MemberTokenMapper memberTokenMapper;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberTokenRepository memberTokenRepository, MemberTokenMapper memberTokenMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberTokenRepository = memberTokenRepository;
        this.memberTokenMapper = memberTokenMapper;
    }

    @Transactional
    public AuthTokenResponseDTO refreshAccessToken(String refreshTokenHeader) {
        String refreshToken = refreshTokenHeader.replace("Bearer ", "").trim();

        if (jwtTokenProvider.isRefreshTokenExpired(refreshToken)) {
            throw new GlobalException(GlobalExceptionCode.INVALID_REFRESH_TOKEN);
        }

        Long memberId = jwtTokenProvider.getMemberIdFromToken(refreshToken);
        MemberToken memberToken = memberTokenRepository.findByMemberPk(memberId)
                .orElseThrow(() -> new GlobalException(GlobalExceptionCode.MEMBER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.generateToken(memberId);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(memberId);

        MemberToken updatedToken = memberTokenMapper.updateToken(memberToken, newAccessToken, newRefreshToken);
        memberTokenRepository.save(updatedToken);

        return new AuthTokenResponseDTO(newAccessToken, newRefreshToken);
    }
}

