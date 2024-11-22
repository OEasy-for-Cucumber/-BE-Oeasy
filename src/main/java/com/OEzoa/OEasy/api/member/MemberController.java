package com.OEzoa.OEasy.api.member;

import com.OEzoa.OEasy.application.member.MemberService;
import com.OEzoa.OEasy.application.member.TokenValidator;
import com.OEzoa.OEasy.application.member.dto.MemberDTO;
import com.OEzoa.OEasy.application.member.dto.MemberDeleteRequestDTO;
import com.OEzoa.OEasy.application.member.dto.MemberSignUpDTO;
import com.OEzoa.OEasy.application.member.dto.MemberSignUpResponseDTO;
import com.OEzoa.OEasy.application.member.dto.NicknameRequestDTO;
import com.OEzoa.OEasy.application.member.dto.NicknameResponseDTO;
import com.OEzoa.OEasy.application.member.dto.PasswordChangeRequestDTO;
import com.OEzoa.OEasy.application.member.mapper.MemberMapper;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.util.HeaderUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Member API", description = "회원 가입 및 회원 정보 관리를 제공합니다.")
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final TokenValidator tokenValidator;

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
    public ResponseEntity<MemberSignUpResponseDTO> register(@RequestBody MemberSignUpDTO memberSignUpDTO) {
        MemberSignUpResponseDTO responseDTO = memberService.registerMember(memberSignUpDTO);
        return ResponseEntity.ok(responseDTO);
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
    public ResponseEntity<MemberDTO> getProfile(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        log.info("전달 받은 액세스 토큰: {}", authorizationHeader);

//        try {
        // Bearer 접두사 제거 후 토큰 유효성 검증 및 사용자 정보 조회
        String accessToken = HeaderUtils.extractTokenFromHeader(authorizationHeader);
        Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);
        MemberDTO memberDTO = MemberMapper.toDto(member);

        log.info("회원정보 조회 성공 email: {}", member.getEmail());
        return ResponseEntity.ok(memberDTO);
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
        String accessToken = HeaderUtils.extractTokenFromHeader(authorizationHeader);
        NicknameResponseDTO response = memberService.modifyNickname(nicknameRequest, accessToken);
        return ResponseEntity.ok(response);
    }


    // 프로필 사진 선택 (S3 버킷에 저장)
    @PatchMapping("/profile-picture")
    @Operation(
            summary = "프로필 사진 변경",
            description = "회원의 프로필 사진을 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "프로필 사진 변경 성공."),
                    @ApiResponse(responseCode = "400", description = "프로필 사진 변경 실패.")
            }
    )
    public ResponseEntity<?> updateProfilePicture(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam("file") MultipartFile file) {
        String accessToken = HeaderUtils.extractTokenFromHeader(authorizationHeader);
        String newImageUrl = memberService.updateProfilePicture(file, accessToken);
        return ResponseEntity.ok(Map.of("message", "프로필 사진 변경 성공", "imageUrl", newImageUrl));

    }

    // 비밀번호 변경
    @PatchMapping("/password")
    @Operation(
            summary = "비밀번호 변경",
            description = "기존 비밀번호를 확인한 후 새로운 비밀번호로 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공."),
                    @ApiResponse(responseCode = "400", description = "비밀번호 변경 실패.")
            }
    )
    public ResponseEntity<String> updatePassword(
            @RequestBody PasswordChangeRequestDTO passwordChangeRequest,
            @RequestHeader(name = "Authorization") String authorizationHeader) {
        String accessToken = HeaderUtils.extractTokenFromHeader(authorizationHeader);
        memberService.changePassword(passwordChangeRequest, accessToken);
        return ResponseEntity.ok("비밀번호 변경 성공");
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    @Operation(
            summary = "회원 탈퇴",
            description = "회원 탈퇴 요청을 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "탈퇴 성공"),
                    @ApiResponse(responseCode = "400", description = "탈퇴 실패: 확인 메시지 불일치")
            }
    )
    public ResponseEntity<String> deleteMember(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @RequestBody MemberDeleteRequestDTO deleteRequest) {
        String accessToken = HeaderUtils.extractTokenFromHeader(authorizationHeader);
        memberService.deleteMember(deleteRequest, accessToken);
        return ResponseEntity.ok("회원 탈퇴가 성공적으로 처리되었습니다.");
    }
}
