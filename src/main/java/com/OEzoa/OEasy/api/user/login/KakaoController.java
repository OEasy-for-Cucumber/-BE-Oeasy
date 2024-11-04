package com.OEzoa.OEasy.api.user.login;

import com.OEzoa.OEasy.application.user.UserService;
import com.OEzoa.OEasy.application.user.dto.UserLoginResponseDTO;
import com.OEzoa.OEasy.infra.api.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Kakao Login API", description = "카카오 로그인 기능을 제공합니다.")
@RequestMapping("kakao")
public class KakaoController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @GetMapping("/callback")
    @Operation(
            summary = "카카오 API 로그인",
            description = "카카오 API를 통해 사용자 정보를 받아 로그인을 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공. JWT 토큰 반환."),
                    @ApiResponse(responseCode = "400", description = "알 수 없는 오류 발생.")
            }
    )

    public ResponseEntity<UserLoginResponseDTO> kakaoCallback(@RequestParam("code") String code, HttpSession session) {
        try {
            UserLoginResponseDTO responseDTO = userService.loginWithKakao(code, session);
            return ResponseEntity.ok(responseDTO); // 로그인 성공 시 JWT 토큰 반환
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 로그인 페이지 이동
    @GetMapping("/login")
    @Operation(
            summary = "카카오 로그인 페이지 이동",
            description = "카카오 로그인 화면으로 리다이렉션됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 로그인 페이지로 이동.")
            }
    )
    public String redirectToKakao() {
        return "redirect:" + kakaoService.getKakaoLogin();
    }
}
