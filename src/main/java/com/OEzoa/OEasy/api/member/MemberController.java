package com.OEzoa.OEasy.api.member;

import com.OEzoa.OEasy.application.member.MemberService;
import com.OEzoa.OEasy.application.member.dto.MemberDTO;
import com.OEzoa.OEasy.application.member.dto.MemberSignUpDTO;
import com.OEzoa.OEasy.application.member.mapper.MemberMapper;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.domain.member.MemberToken;
import com.OEzoa.OEasy.domain.member.MemberTokenRepository;
import com.OEzoa.OEasy.util.JwtTokenProvider;
import com.OEzoa.OEasy.util.s3Bucket.FileUploader;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member API", description = "회원 가입 및 회원 정보 관리를 제공합니다.")
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final FileUploader fileUploader;
    private final MemberTokenRepository memberTokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
    public ResponseEntity<?> getProfile(@CookieValue(name = "accessToken", required = false) String accessToken) {
        try {
            MemberDTO memberDTO = validateMemberAccessToken(accessToken);
            return ResponseEntity.ok(memberDTO);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // 닉네임 변경
    @PatchMapping("/nickname")
    @Operation(
            summary = "닉네임 변경",
            description = "회원의 닉네임을 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "닉네임 변경 성공."),
                    @ApiResponse(responseCode = "400", description = "닉네임 변경 실패.")
            }
    )
    public ResponseEntity<?> updateNickname(@RequestParam String newNickname,
                                            @CookieValue(name = "accessToken", required = false) String accessToken) {
        try {
            Member member = validateMemberAccessTokenAndReturnMember(accessToken);
            member = member.toBuilder().nickname(newNickname).build();
            memberRepository.save(member);
            return ResponseEntity.ok("닉네임 변경 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("닉네임 변경 실패: " + e.getMessage());
        }
    }

    // 프로필 사진 선택 (S3 버킷에 저장)
    @PatchMapping("/profile-picture")
    @Operation(
            summary = "프로필 사진 선택",
            description = "회원의 프로필 사진을 선택하여 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "프로필 사진 변경 성공."),
                    @ApiResponse(responseCode = "400", description = "프로필 사진 변경 실패.")
            }
    )
    public ResponseEntity<?> updateProfilePicture(@RequestParam String imageName, @RequestParam String imageUri,
                                                  @CookieValue(name = "accessToken", required = false) String accessToken) {
        try {
            Member member = validateMemberAccessTokenAndReturnMember(accessToken);
            // S3 버킷에 이미지 업로드
            String s3ImageUrl = fileUploader.uploadImage(imageName, imageUri);
            member = member.toBuilder().memberImage(s3ImageUrl).build();
            memberRepository.save(member);
            return ResponseEntity.ok("프로필 사진 변경 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("프로필 사진 변경 실패: " + e.getMessage());
        }
    }

    // 액세스 토큰 검증 및 사용자 정보 조회 메서드
    private MemberDTO validateMemberAccessToken(String accessToken) throws Exception {
        return MemberMapper.toDto(validateMemberAccessTokenAndReturnMember(accessToken));
    }

    // 액세스 토큰 검증 및 Member 객체 조회 메서드
    private Member validateMemberAccessTokenAndReturnMember(String accessToken) throws Exception {
        if (accessToken == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다. 액세스 토큰이 존재하지 않습니다.");
        }

        try {
            // JWT 토큰에서 사용자 ID 추출
            Long memberId = jwtTokenProvider.getMemberIdFromToken(accessToken);
            return memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. 잘못된 사용자 ID입니다."));
        } catch (ExpiredJwtException e) {
            // 토큰 만료 시 새로운 토큰 발급 로직 추가
            String refreshToken = getRefreshTokenForMember(accessToken);
            if (refreshToken == null) {
                throw new IllegalStateException("토큰이 만료되었습니다. 다시 로그인 해 주세요.", e);
            }
            String newAccessToken = jwtTokenProvider.generateToken(jwtTokenProvider.getMemberIdFromToken(refreshToken));
            return memberRepository.findById(jwtTokenProvider.getMemberIdFromToken(newAccessToken))
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. 잘못된 사용자 ID입니다."));
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("지원하지 않는 JWT 토큰입니다.", e);
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException("잘못된 JWT 토큰 형식입니다.", e);
        } catch (Exception e) {
            throw new IllegalStateException("유효하지 않은 토큰입니다.", e);
        }
    }

    // 토큰 갱신 메서드 추가
    private String getRefreshTokenForMember(String accessToken) {
        // 토큰에서 사용자 ID 추출
        Long memberId = jwtTokenProvider.getMemberIdFromToken(accessToken);
        MemberToken memberToken = memberTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. 잘못된 사용자 ID입니다."));

        if (jwtTokenProvider.isTokenExpired(memberToken.getRefreshToken())) {
            throw new IllegalStateException("리프레시 토큰이 만료되었습니다. 다시 로그인 해 주세요.");
        }
        return memberToken.getRefreshToken();
    }

}
