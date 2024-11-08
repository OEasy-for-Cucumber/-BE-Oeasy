package com.OEzoa.OEasy.application.member;

import com.OEzoa.OEasy.application.member.dto.KakaoDTO;
import com.OEzoa.OEasy.application.member.dto.MemberLoginResponseDTO;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.infra.api.KakaoService;
import com.OEzoa.OEasy.util.JwtTokenProvider;
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
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private KakaoService kakaoService;

    // 카카오 로그인 처리 (JWT 발급)
    public MemberLoginResponseDTO loginWithKakao(String code, HttpSession session) throws Exception {
        // 카카오 API를 사용하여 사용자 정보 가져오기
        KakaoDTO kakaoInfo = kakaoService.getKakaoInfo(code);
        log.info("카카오 사용자 정보 수신: " + kakaoInfo);

        // 사용자 조회 또는 신규 사용자 등록
        Member member = memberRepository.findByKakaoId(kakaoInfo.getId()).orElse(null);
        if (member == null) {
            log.info("사용자가 없으므로 신규 등록 진행");
            member = Member.builder()
                    .kakaoId(kakaoInfo.getId())
                    .email(kakaoInfo.getEmail())
                    .nickname(kakaoInfo.getNickname())
                    .build();
            member = memberRepository.save(member);
            log.info("신규 사용자 저장 완료: " + member);
        } else {
            log.info("기존 사용자 발견: " + member);
        }

        // JWT 토큰 발급
        String jwtAccessToken = jwtTokenProvider.generateToken(member.getMemberPk());
        session.setAttribute("accessToken", jwtAccessToken);
        log.info("카카오 로그인 성공. 생성된 JWT 토큰: " + jwtAccessToken);

        return MemberLoginResponseDTO.builder()
                .accessToken(jwtAccessToken)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}