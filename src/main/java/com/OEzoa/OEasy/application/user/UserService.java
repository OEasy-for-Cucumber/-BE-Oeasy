package com.OEzoa.OEasy.application.user;

import com.OEzoa.OEasy.application.user.dto.KakaoDTO;
import com.OEzoa.OEasy.application.user.dto.UserLoginResponseDTO;
import com.OEzoa.OEasy.domain.user.User;
import com.OEzoa.OEasy.domain.user.UserRepository;
import com.OEzoa.OEasy.domain.user.UserToken;
import com.OEzoa.OEasy.domain.user.UserTokenRepository;
import com.OEzoa.OEasy.infra.api.KakaoService;
import com.OEzoa.OEasy.util.JwtTokenProvider;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private KakaoService kakaoService;

    public UserLoginResponseDTO loginWithKakao(String code, HttpSession session)
            throws Exception {
        // 카카오 API를 사용하여 사용자 정보 가져오기
        KakaoDTO kakaoInfo = kakaoService.getKakaoInfo(code);
        log.info("카카오 사용자 정보 수신: " + kakaoInfo);

        // 사용자 조회 또는 신규 사용자 등록
        User user = userRepository.findByKakaoId(kakaoInfo.getId());
        if (user == null) {
            log.info("사용자가 없으므로 신규 등록 진행");
            try {
                // 신규 사용자 등록
                user = User.builder()
                        .kakaoId(kakaoInfo.getId())
//                        .userPk(user.getUserPk()) // userPk는 @MapsID 로 이미 명시되어 있어서 오류가 발생했었다.
                        .email(kakaoInfo.getEmail())
                        .nickname(kakaoInfo.getNickname())
                        .build();
                user = userRepository.save(user);
                log.info("신규 사용자 저장 완료: " + user);
            } catch (Exception e) {
                log.error("사용자 저장 중 오류 발생", e);
                throw e;
            }
        } else {
            log.info("기존 사용자 발견: " + user);
        }

        // JWT 토큰 발급
        String jwtAccessToken = jwtTokenProvider.generateToken(user.getUserPk());
        String jwtRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUserPk());
        log.info("생성된 Access Token: " + jwtAccessToken);
        log.info("생성된 Refresh Token: " + jwtRefreshToken);

        // UserToken 저장
        UserToken userToken = userTokenRepository.findById(user.getUserPk()).orElse(null);
        if (userToken == null) {
            // 신규 UserToken 생성
            userToken = UserToken.builder()
                    .user(user)
                    .accessToken(jwtAccessToken)
                    .refreshToken(jwtRefreshToken)
                    .build();
            log.info("신규 UserToken 생성: " + userToken);
        } else {
            // 기존 UserToken 업데이트
            userToken = userToken.toBuilder()
                    .accessToken(jwtAccessToken)
                    .refreshToken(jwtRefreshToken)
                    .build();
            log.info("기존 UserToken 업데이트: " + userToken);
        }

        userToken = userTokenRepository.save(userToken);
        log.info("UserToken 저장 완료: " + userToken);

        session.setAttribute("userId", user.getUserPk());
        session.setAttribute("accessToken", jwtAccessToken);

        return new UserLoginResponseDTO(jwtAccessToken, user.getEmail(), user.getNickname());
    }
}
