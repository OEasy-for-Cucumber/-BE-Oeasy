package com.OEzoa.OEasy.api.user.login;

import com.OEzoa.OEasy.infra.api.KakaoService;
import com.OEzoa.OEasy.application.user.dto.KakaoDTO;
import com.OEzoa.OEasy.application.user.dto.KakaoResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("kakao")
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/callback")
    public ResponseEntity<KakaoResponseDTO<KakaoDTO>> callback(HttpServletRequest request) throws Exception {
        KakaoDTO kakaoInfo = kakaoService.getKakaoInfo(request.getParameter("code"));

        return ResponseEntity.ok()
                .body(new KakaoResponseDTO<>("성공", kakaoInfo));
    }
}
