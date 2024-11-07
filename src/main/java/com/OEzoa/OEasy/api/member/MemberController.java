package com.OEzoa.OEasy.api.member;

import com.OEzoa.OEasy.application.member.MemberService;
import com.OEzoa.OEasy.application.member.dto.MemberSignUpDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member API", description = "회원 가입 및 회원 정보 관리를 제공합니다.")
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

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
    public ResponseEntity<?> getProfile() {
        // 회원 정보 조회 로직 추가해야해이예에~
        return ResponseEntity.ok("회원 정보 조회 성공");
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
    public ResponseEntity<?> updateNickname(@RequestParam String newNickname) {
        // 닉네임 변경 로직 추가 필요
        return ResponseEntity.ok("닉네임 변경 성공");
    }

    // 프로필 사진 선택
    @PatchMapping("/profile-picture")
    @Operation(
            summary = "프로필 사진 선택",
            description = "회원의 프로필 사진을 선택하여 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "프로필 사진 변경 성공."),
                    @ApiResponse(responseCode = "400", description = "프로필 사진 변경 실패.")
            }
    )
    public ResponseEntity<?> updateProfilePicture(@RequestParam String newProfilePictureUrl) {
        // 프로필 사진 변경 로직 추가 필요함함함
        return ResponseEntity.ok("프로필 사진 변경 성공");
    }
}