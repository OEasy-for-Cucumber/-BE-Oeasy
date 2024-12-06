package com.OEzoa.OEasy.application.member;

import com.OEzoa.OEasy.application.member.dto.KakaoDTO;
import com.OEzoa.OEasy.application.member.dto.MemberLoginResponseDTO;
import com.OEzoa.OEasy.application.member.mapper.KakaoMapper;
import com.OEzoa.OEasy.application.member.mapper.MemberMapper;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.domain.member.MemberToken;
import com.OEzoa.OEasy.domain.member.MemberTokenRepository;
import com.OEzoa.OEasy.infra.api.KakaoService;
import com.OEzoa.OEasy.util.member.JwtTokenProvider;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KakaoMemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberTokenRepository memberTokenRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private KakaoService kakaoService;
    @Autowired
    private KakaoMapper kakaoMapper;

    // 카카오 로그인 처리 (JWT 발급)
    public MemberLoginResponseDTO loginWithKakao(String code, HttpSession session) throws Exception {
        KakaoDTO kakaoInfo = kakaoService.getKakaoInfo(code);
        log.info("카카오 사용자 정보 수신: " + kakaoInfo);

        Member member = memberRepository.findByKakaoId(kakaoInfo.getId()).orElse(null);
        if (member == null) {
            member = kakaoMapper.toMember(kakaoInfo);
            member = memberRepository.save(member);
            log.info("신규 사용자 저장 완료: " + member);
        } else {
            log.info("기존 사용자 발견: " + member);
        }

        // JWT 액세스 토큰 및 리프레시 토큰 발급
        String jwtAccessToken = jwtTokenProvider.generateToken(member.getMemberPk());
        String jwtRefreshToken = jwtTokenProvider.generateRefreshToken(member.getMemberPk());
        session.setAttribute("accessToken", jwtAccessToken);
        session.setAttribute("refreshToken", jwtRefreshToken);
        log.info("카카오 로그인 성공. 생성된 액세스 토큰: {}, 리프레시 토큰: {}", jwtAccessToken, jwtRefreshToken);
        // MemberToken 저장 및 업데이트
        MemberToken memberToken = memberTokenRepository.findById(member.getMemberPk()).orElse(null);

        if (memberToken == null) {
            log.info("신규 MemberToken 생성 진행");
            memberToken = kakaoMapper.createMemberToken(member, jwtAccessToken, jwtRefreshToken);
        } else {
            log.info("기존 MemberToken 업데이트 진행");
            memberToken = kakaoMapper.updateMemberToken(memberToken, jwtAccessToken, jwtRefreshToken);
        }

        memberTokenRepository.save(memberToken);
        log.info("MemberToken 저장 완료: {}", memberToken);
        return kakaoMapper.toLoginResponseDTO(member, jwtAccessToken, jwtRefreshToken);
    }
}