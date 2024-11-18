package com.OEzoa.OEasy.api.member;

import com.OEzoa.OEasy.application.member.MemberService;
import com.OEzoa.OEasy.application.member.TokenValidator;
import com.OEzoa.OEasy.application.member.dto.MemberDTO;
import com.OEzoa.OEasy.application.member.dto.MemberSignUpDTO;
import com.OEzoa.OEasy.application.member.dto.NicknameRequestDTO;
import com.OEzoa.OEasy.application.member.dto.NicknameResponseDTO;
import com.OEzoa.OEasy.application.member.dto.ProfilePictureRequestDTO;
import com.OEzoa.OEasy.application.member.mapper.MemberMapper;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.domain.member.MemberTokenRepository;
import com.OEzoa.OEasy.util.s3Bucket.FileUploader;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Member API", description = "회원 가입 및 회원 정보 관리를 제공합니다.")
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final FileUploader fileUploader;
    private final MemberTokenRepository memberTokenRepository;
    private final TokenValidator tokenValidator;

    // Bearer 토큰에서 실제 토큰을 추출하는 메서드
    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer " 이후의 토큰만 추출
        } else {
            throw new IllegalArgumentException("올바르지 않은 인증 헤더 형식입니다.");
        }
    }

    // 일반 회원가입
    @PostMapping("/signup")
    @Operation(
            summary = "회원가입",
            description = "회원가입 및 회원가입 성공 여부를 반환",
            responses = {
                    @ApiResponse(responseCode = "201", description = "회원가입 성공."),
                    @ApiResponse(responseCode = "409", description = "중복된 이메일로 인한 회원가입 실패.")
            }
    )
    public ResponseEntity<String> register(@RequestBody MemberSignUpDTO memberSignUpDTO) {
        try {
            memberService.registerMember(memberSignUpDTO);
            return ResponseEntity.status(201).body("회원가입 성공");
        } catch (Exception e) {
            if (e.getMessage().equals("이미 존재하는 이메일입니다.")) {
                return ResponseEntity.status(409).body("중복된 이메일입니다.");
            }
            return ResponseEntity.status(400).body("회원가입 실패");
        }
    }

    // 회원 정보 조회
    @GetMapping("/profile")
    @Operation(
            summary = "회원 정보 조회",
            description = "로그인한 회원의 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공."),
                    @ApiResponse(responseCode = "401", description = "조회 실패: 인증되지 않은 사용자.")
            }
    )
    public ResponseEntity<?> getProfile(@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        log.info("전달 받은 액세스 토큰: {}", authorizationHeader);

        try {
            // Bearer 접두사 제거 후 토큰 유효성 검증 및 사용자 정보 조회
            String accessToken = extractTokenFromHeader(authorizationHeader);
            Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);
            MemberDTO memberDTO = MemberMapper.toDto(member);

            log.info("회원정보 조회 성공 email: {}", member.getEmail());
            return ResponseEntity.ok(memberDTO);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("액세스 토큰이 만료되었습니다. 리프레시 토큰을 포함하여 다시 요청하세요.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
        }
    }

    @PatchMapping("/nickname")
    @Operation(
            summary = "닉네임 변경",
            description = "회원의 닉네임을 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "닉네임 변경 성공."),
                    @ApiResponse(responseCode = "400", description = "닉네임 변경 실패.")
            }
    )
    public ResponseEntity<NicknameResponseDTO> modifyNickname(
            @RequestBody NicknameRequestDTO nicknameRequest,
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        String accessToken = extractTokenFromHeader(authorizationHeader);
        NicknameResponseDTO response = memberService.modifyNickname(nicknameRequest, accessToken);
        return ResponseEntity.ok(response);
    }


    // 프로필 사진 선택 (S3 버킷에 저장)
    @PatchMapping("/profile-picture")
    @Operation(
            summary = "프로필 사진 변경",
            description = "회원의 닉네임과 이미지 URL을 제공하여 프로필 사진을 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "프로필 사진 변경 성공. 업로드된 이미지 URL 반환."),
                    @ApiResponse(responseCode = "400", description = "프로필 사진 변경 실패. 잘못된 요청 데이터."),
                    @ApiResponse(responseCode = "401", description = "인증 실패. 유효하지 않은 액세스 토큰.")
            }
    )
    public ResponseEntity<String> updateProfilePicture(
            @RequestBody ProfilePictureRequestDTO profilePictureRequest,
            @RequestHeader(name = "Authorization") String authorizationHeader) {

        String accessToken = extractTokenFromHeader(authorizationHeader);
        String newImageUrl = memberService.updateProfileImage(
                profilePictureRequest.getNickname(),
                profilePictureRequest.getImageUrl(),
                accessToken
        );

        return ResponseEntity.ok(newImageUrl);
    }
}
