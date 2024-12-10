package com.OEzoa.OEasy.api.member.login;

import com.OEzoa.OEasy.application.member.KakaoMemberService;
import com.OEzoa.OEasy.application.member.MemberService;
import com.OEzoa.OEasy.application.member.dto.MemberLoginDTO;
import com.OEzoa.OEasy.application.member.dto.MemberLoginResponseDTO;
import com.OEzoa.OEasy.infra.api.KakaoService;
import com.OEzoa.OEasy.util.member.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Login API", description = "일반 로그인 및 카카오 로그인을 제공합니다.")
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private KakaoMemberService kakaoMemberService;
    @Autowired
    private KakaoService kakaoService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 일반 로그인
    @PostMapping("/oeasy")
    @Operation(
            summary = "일반 로그인",
            description = "일반 로그인을 처리하고 JWT 토큰을 반환합니다."
    )
    public ResponseEntity<MemberLoginResponseDTO> login(
            @RequestBody MemberLoginDTO memberLoginDTO,
            HttpServletResponse response) {

        MemberLoginResponseDTO responseDTO = memberService.login(memberLoginDTO, response);
        return ResponseEntity.ok(
                new MemberLoginResponseDTO(responseDTO.getAccessToken(), responseDTO.getEmail(), responseDTO.getNickname())
        );
    }

    // 카카오 로그인 콜백
    @PostMapping("/kakao/callback")
    @Operation(
            summary = "카카오 API 로그인",
            description = "카카오 API를 통해 사용자 정보를 받아 로그인을 처리하고 JWT를 반환합니다."
    )
    public ResponseEntity<MemberLoginResponseDTO> kakaoCallback(
            @RequestParam("code") String code,
            HttpServletResponse response) throws Exception {
        MemberLoginResponseDTO responseDTO = kakaoMemberService.loginWithKakao(code, response);
        return ResponseEntity.ok(
                new MemberLoginResponseDTO(responseDTO.getAccessToken(), responseDTO.getEmail(), responseDTO.getNickname())
        );
    }

    // 카카오 로그인 URL 제공
    @GetMapping("/kakao")
    @Operation(
            summary = "카카오 로그인 페이지 URL 제공",
            description = "카카오 로그인 URL링크를 제공합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 로그인 페이지 URL 반환.")
            }
    )
    public ResponseEntity<String> getKakaoLoginUrl() {
        return ResponseEntity.ok(kakaoService.getKakaoLogin());
    }
}
