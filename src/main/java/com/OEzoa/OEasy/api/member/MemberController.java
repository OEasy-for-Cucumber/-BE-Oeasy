package com.OEzoa.OEasy.api.member;

import com.OEzoa.OEasy.application.member.MemberService;
import com.OEzoa.OEasy.application.member.SignUpValidator;
import com.OEzoa.OEasy.application.member.TokenValidator;
import com.OEzoa.OEasy.application.member.dto.EmailCheckRequestDTO;
import com.OEzoa.OEasy.application.member.dto.MemberDTO;
import com.OEzoa.OEasy.application.member.dto.MemberDeleteRequestDTO;
import com.OEzoa.OEasy.application.member.dto.MemberSignUpResponseDTO;
import com.OEzoa.OEasy.application.member.dto.NicknameCheckRequestDTO;
import com.OEzoa.OEasy.application.member.dto.NicknameRequestDTO;
import com.OEzoa.OEasy.application.member.dto.NicknameResponseDTO;
import com.OEzoa.OEasy.application.member.dto.PasswordChangeRequestDTO;
import com.OEzoa.OEasy.application.member.dto.PasswordValidationRequestDTO;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.util.HeaderUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final SignUpValidator signUpValidator;

    @PostMapping("/check-email")
    @Operation(
            summary = "이메일 중복 확인",
            description = "입력받은 이메일의 중복 여부를 확인하고, 중복이 없을 경우 JWT 토큰을 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이메일 사용 가능."),
                    @ApiResponse(responseCode = "409", description = "중복된 이메일로 인한 실패.")
            }
    )
    public ResponseEntity<String> checkEmail(@RequestBody EmailCheckRequestDTO emailCheckRequestDTO) {
        if (!signUpValidator.isValidEmail(emailCheckRequestDTO.getEmail())) {
            throw new GlobalException(GlobalExceptionCode.INVALID_EMAIL);
        }
        String token = memberService.checkEmailAndGenerateToken(emailCheckRequestDTO.getEmail());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/check-nickname")
    @Operation(
            summary = "닉네임 중복 확인",
            description = "입력받은 닉네임의 중복 여부를 확인하고, 중복이 없을 경우 JWT 토큰을 업데이트하여 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "닉네임 사용 가능."),
                    @ApiResponse(responseCode = "409", description = "중복된 닉네임으로 인한 실패.")
            }
    )
    public ResponseEntity<String> checkNickname(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestBody NicknameCheckRequestDTO nicknameCheckRequestDTO
    ) {
        if (!signUpValidator.isValidNickname(nicknameCheckRequestDTO.getNickname())) {
            throw new GlobalException(GlobalExceptionCode.INVALID_NICKNAME);
        }
        String token = HeaderUtils.extractTokenFromHeader(authorizationHeader);
        String updatedToken = memberService.checkNicknameAndUpdateToken(token, nicknameCheckRequestDTO.getNickname());
        return ResponseEntity.ok(updatedToken);
    }

    @PostMapping("/check-password")
    @Operation(
            summary = "비밀번호 유효성 검증",
            description = "입력받은 비밀번호의 유효성을 검증하고, 검증이 완료된 경우 JWT 토큰을 업데이트하여 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 유효."),
                    @ApiResponse(responseCode = "400", description = "비밀번호가 유효하지 않음.")
            }
    )
    public ResponseEntity<String> validatePassword(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestBody PasswordValidationRequestDTO passwordValidationRequestDTO
    ) {
        if (!signUpValidator.isValidPassword(passwordValidationRequestDTO.getPassword())) {
            throw new GlobalException(GlobalExceptionCode.INVALID_PASSWORD);
        }

        String token = HeaderUtils.extractTokenFromHeader(authorizationHeader);
        String updatedToken = memberService.validatePasswordAndUpdateToken(token, passwordValidationRequestDTO.getPassword());
        return ResponseEntity.ok(updatedToken);
    }

    @PostMapping("/signup")
    @Operation(
            summary = "회원가입",
            description = "회원가입 및 회원가입 성공 여부를 반환",
            responses = {
                    @ApiResponse(responseCode = "201", description = "회원가입 성공."),
                    @ApiResponse(responseCode = "400", description = "회원가입 데이터가 유효하지 않음.")
            }
    )
    public ResponseEntity<MemberSignUpResponseDTO> register(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader
    ) {
        String token = HeaderUtils.extractTokenFromHeader(authorizationHeader);
        MemberSignUpResponseDTO responseDTO = memberService.registerMember(token);
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
        MemberDTO memberDTO = memberService.getProfile(authorizationHeader);
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
