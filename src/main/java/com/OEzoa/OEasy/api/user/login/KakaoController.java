package com.OEzoa.OEasy.api.user.login;

import com.OEzoa.OEasy.application.user.UserService;
import com.OEzoa.OEasy.application.user.dto.UserLoginResponseDTO;
import com.OEzoa.OEasy.infra.api.KakaoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("kakao")
public class KakaoController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @GetMapping("/callback")
    public ResponseEntity<UserLoginResponseDTO> kakaoCallback(@RequestParam("code") String code, HttpSession session) {

//            // HttpServletResponse를 UserService로 전달하여 쿠키 설정을 처리
//            UserLoginResponseDTO responseDTO = userService.loginWithKakao(code, response);
//            return ResponseEntity.ok(responseDTO); // 로그인 성공 시 JWT 토큰 반환

        try {
            UserLoginResponseDTO responseDTO = userService.loginWithKakao(code, session);
            return ResponseEntity.ok(responseDTO); // 로그인 성공 시 JWT 토큰 반환
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 로그인 페이지 이동
    @GetMapping("/login")
    public String redirectToKakao() {
        return "redirect:" + kakaoService.getKakaoLogin();
    }
}
