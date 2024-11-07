package com.OEzoa.OEasy.api.member.login;

import com.OEzoa.OEasy.application.member.KakaoMemberService;
import com.OEzoa.OEasy.application.member.MemberService;
import com.OEzoa.OEasy.application.member.dto.MemberLoginDTO;
import com.OEzoa.OEasy.application.member.dto.MemberLoginResponseDTO;
import com.OEzoa.OEasy.infra.api.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Login API", description = "일반 로그인 및 카카오 로그인을 제공합니다.")
@RequestMapping("/login")
public class LoginController {

    private final MemberService memberService;
    private final KakaoMemberService kakaoMemberService;
    private final KakaoService kakaoService;

    // 일반 로그인 (JWT 활용)
    @PostMapping("/oeasy")
    @Operation(
            summary = "일반 로그인",
            description = "일반 로그인을 처리하고 JWT 토큰을 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공. JWT 토큰 반환."),
                    @ApiResponse(responseCode = "401", description = "로그인 실패.")
            }
    )
    public ResponseEntity<MemberLoginResponseDTO> login(@RequestBody MemberLoginDTO memberLoginDTO, HttpSession session) {
        try {
            MemberLoginResponseDTO responseDTO = memberService.login(memberLoginDTO, session);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    // 카카오 로그인 콜백
    @GetMapping("/kakao/callback")
    @Operation(
            summary = "카카오 API 로그인",
            description = "카카오 API를 통해 사용자 정보를 받아 로그인을 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공. JWT 토큰 반환."),
                    @ApiResponse(responseCode = "400", description = "알 수 없는 오류 발생.")
            }
    )
    public ResponseEntity<MemberLoginResponseDTO> kakaoCallback(@RequestParam("code") String code, HttpSession session) {
        try {
            MemberLoginResponseDTO responseDTO = kakaoMemberService.loginWithKakao(code, session);
            return ResponseEntity.ok(responseDTO); // 로그인 성공 시 JWT 토큰 반환
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // 오류 발생 시 ResponseEntity를 반환
        }
    }

    // 카카오 로그인 URL 제공
    @GetMapping("/kakao")
    @Operation(
            summary = "카카오 로그인 페이지 URL 제공",
            description = "카카오 로그인 페이지로 리디렉션할 수 있는 URL을 제공합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 로그인 페이지 URL 반환.")
            }
    )
    public ResponseEntity<String> getKakaoLoginUrl() {
        return ResponseEntity.ok(kakaoService.getKakaoLogin());
    }
}
