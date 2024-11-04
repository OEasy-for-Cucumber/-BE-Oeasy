package com.OEzoa.OEasy.application.member;

import com.OEzoa.OEasy.application.member.dto.KakaoDTO;
import com.OEzoa.OEasy.application.member.dto.MemberLoginResponseDTO;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.domain.member.MemberToken;
import com.OEzoa.OEasy.domain.member.MemberTokenRepository;
import com.OEzoa.OEasy.infra.api.KakaoService;
import com.OEzoa.OEasy.util.JwtTokenProvider;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberTokenRepository memberTokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private KakaoService kakaoService;

    public MemberLoginResponseDTO loginWithKakao(String code, HttpSession session)
            throws Exception {
        // 카카오 API를 사용하여 사용자 정보 가져오기
        KakaoDTO kakaoInfo = kakaoService.getKakaoInfo(code);
        log.info("카카오 사용자 정보 수신: " + kakaoInfo);

        // 사용자 조회 또는 신규 사용자 등록
        Member member = memberRepository.findByKakaoId(kakaoInfo.getId());
        if (member == null) {
            log.info("사용자가 없으므로 신규 등록 진행");
            try {
                // 신규 사용자 등록
                member = Member.builder()
                        .kakaoId(kakaoInfo.getId())
//                        .membePk(membe.getMemberPk()) // membePk는 @MapsID 로 이미 명시되어 있어서 오류가 발생했었다.
                        .email(kakaoInfo.getEmail())
                        .nickname(kakaoInfo.getNickname())
                        .build();
                member = memberRepository.save(member);
                log.info("신규 사용자 저장 완료: " + member);
            } catch (Exception e) {
                log.error("사용자 저장 중 오류 발생", e);
                throw e;
            }
        } else {
            log.info("기존 사용자 발견: " + member);
        }

        // JWT 토큰 발급
        String jwtAccessToken = jwtTokenProvider.generateToken(member.getMemberPk());
        String jwtRefreshToken = jwtTokenProvider.generateRefreshToken(member.getMemberPk());
        log.info("생성된 Access Token: " + jwtAccessToken);
        log.info("생성된 Refresh Token: " + jwtRefreshToken);

        // MemberToken 저장
        MemberToken MemberToken = memberTokenRepository.findById(member.getMemberPk()).orElse(null);
        if (MemberToken == null) {
            // 신규 MemberToken 생성
            MemberToken = MemberToken.builder()
                    .member(member)
                    .accessToken(jwtAccessToken)
                    .refreshToken(jwtRefreshToken)
                    .build();
            log.info("신규 MemberToken 생성: " + MemberToken);
        } else {
            // 기존 MemberToken 업데이트
            MemberToken = MemberToken.toBuilder()
                    .accessToken(jwtAccessToken)
                    .refreshToken(jwtRefreshToken)
                    .build();
            log.info("기존 MemberToken 업데이트: " + MemberToken);
        }

        MemberToken = memberTokenRepository.save(MemberToken);
        log.info("MemberToken 저장 완료: " + MemberToken);

        session.setAttribute("membeId", member.getMemberPk());
        session.setAttribute("accessToken", jwtAccessToken);

        return new MemberLoginResponseDTO(jwtAccessToken, member.getEmail(), member.getNickname());
    }
}
