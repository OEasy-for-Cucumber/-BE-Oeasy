package com.OEzoa.OEasy.application.member;

import com.OEzoa.OEasy.application.member.dto.AuthTokenResponseDTO;
import com.OEzoa.OEasy.application.member.mapper.MemberTokenMapper;
import com.OEzoa.OEasy.domain.member.MemberToken;
import com.OEzoa.OEasy.domain.member.MemberTokenRepository;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.util.member.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private MemberTokenRepository memberTokenRepository;
    @Autowired
    private MemberTokenMapper memberTokenMapper;

    @Transactional
    public String refreshAccessToken(String refreshToken, HttpServletResponse response) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(refreshToken);
        MemberToken memberToken = memberTokenRepository.findByMemberPk(memberId)
                .orElseThrow(() -> new GlobalException(GlobalExceptionCode.MEMBER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.generateToken(memberId);
        MemberToken updatedToken = memberTokenMapper.updateToken(memberToken, newAccessToken );
        memberTokenRepository.save(updatedToken);
        log.info("액세스 토큰 갱신 성공 유저 email : {}", updatedToken.getMember().getEmail());
        return refreshToken;
    }
}

