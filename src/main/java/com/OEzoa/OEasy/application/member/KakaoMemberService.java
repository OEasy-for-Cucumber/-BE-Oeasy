package com.OEzoa.OEasy.application.member;

import com.OEzoa.OEasy.application.member.dto.KakaoDTO;
import com.OEzoa.OEasy.application.member.dto.MemberLoginResponseDTO;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.domain.member.MemberToken;
import com.OEzoa.OEasy.domain.member.MemberTokenRepository;
import com.OEzoa.OEasy.infra.api.KakaoService;
import com.OEzoa.OEasy.util.member.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
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

    // 카카오 로그인 처리 (JWT 발급)
    public MemberLoginResponseDTO loginWithKakao(String code, HttpServletResponse response)  throws Exception {
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

        // JWT 액세스 토큰 및 리프레시 토큰 발급
        String jwtAccessToken = jwtTokenProvider.generateToken(member.getMemberPk());
        jwtTokenProvider.createRefreshTokenCookie(member.getMemberPk(), response);
        log.info("카카오 로그인 성공. 생성된 액세스 토큰: {}", jwtAccessToken);

        // MemberToken 저장 및 업데이트
        MemberToken memberToken = memberTokenRepository.findById(member.getMemberPk()).orElse(null);
        if (memberToken == null) {
            // 신규 MemberToken 생성
            memberToken = MemberToken.builder()
                    .member(member)
                    .accessToken(jwtAccessToken)
                    .build();
            log.info("신규 MemberToken 생성: {}", memberToken);
        } else {
            // 기존 MemberToken 업데이트
            memberToken = memberToken.toBuilder()
                    .accessToken(jwtAccessToken)
                    .build();
            log.info("기존 MemberToken 업데이트: {}", memberToken);
        }
        memberTokenRepository.save(memberToken);
        log.info("MemberToken 저장 완료: {}", memberToken);

        log.info("카카오 로그인 성공. 생성된 액세스 토큰: {}", jwtAccessToken);
        return MemberLoginResponseDTO.builder()
                .accessToken(jwtAccessToken)
                .email(member.getEmail())
                .nickname(kakaoInfo.getNickname())
                .build();
    }
}