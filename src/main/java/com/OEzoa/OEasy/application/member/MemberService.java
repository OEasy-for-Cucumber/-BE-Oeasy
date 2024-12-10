package com.OEzoa.OEasy.application.member;

import com.OEzoa.OEasy.application.member.dto.MemberDTO;
import com.OEzoa.OEasy.application.member.dto.MemberDeleteRequestDTO;
import com.OEzoa.OEasy.application.member.dto.MemberLoginDTO;
import com.OEzoa.OEasy.application.member.dto.MemberLoginResponseDTO;
import com.OEzoa.OEasy.application.member.dto.MemberSignUpDTO;
import com.OEzoa.OEasy.application.member.dto.MemberSignUpResponseDTO;
import com.OEzoa.OEasy.application.member.dto.NicknameRequestDTO;
import com.OEzoa.OEasy.application.member.dto.NicknameResponseDTO;
import com.OEzoa.OEasy.application.member.dto.PasswordChangeRequestDTO;
import com.OEzoa.OEasy.application.member.mapper.MemberMapper;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.domain.member.MemberToken;
import com.OEzoa.OEasy.domain.member.MemberTokenRepository;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.util.HeaderUtils;
import com.OEzoa.OEasy.util.member.JwtTokenProvider;
import com.OEzoa.OEasy.util.member.PasswordUtil;
import com.OEzoa.OEasy.util.s3Bucket.FileUploader;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberTokenRepository memberTokenRepository;
    @Autowired
    private TokenValidator tokenValidator;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private FileUploader fileUploader;
    @Autowired
    private MemberMapper memberMapper;

    // 일반 회원 가입
    @Transactional
    public MemberSignUpResponseDTO registerMember(String token) {
        Map<String, Object> data = jwtTokenProvider.extractDataFromToken(token);

        String email = (String) data.get("email");
        String nickname = (String) data.get("nickname");
        String password = (String) data.get("password");

        if (email == null || nickname == null || password == null) {
            throw new GlobalException(GlobalExceptionCode.INVALID_SIGNUP_FLOW);
        }

        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(password, salt);

        Member member = memberMapper.toEntity(email, nickname, hashedPassword, salt);
        memberRepository.save(member);

        return memberMapper.toSignUpResponseDTO(member);
    }

    public String checkEmailAndGenerateToken(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new GlobalException(GlobalExceptionCode.EMAIL_DUPLICATION);
        }
        return jwtTokenProvider.generateTokenWithData(Map.of("email", email));
    }

    public String checkNicknameAndUpdateToken(String token, String nickname) {
        Map<String, Object> data = jwtTokenProvider.extractDataFromToken(token);
        if (memberRepository.existsByNickname(nickname)) {
            throw new GlobalException(GlobalExceptionCode.NICKNAME_DUPLICATION);
        }
        data.put("nickname", nickname);
        return jwtTokenProvider.generateTokenWithData(data);
    }

    public String validatePasswordAndUpdateToken(String token, String password) {
        Map<String, Object> data = jwtTokenProvider.extractDataFromToken(token);

        if (password == null || password.isEmpty()) {
            throw new GlobalException(GlobalExceptionCode.INVALID_PASSWORD);
        }
        data.put("password", password);
        return jwtTokenProvider.generateTokenWithData(data);
    }

    // 회원 정보 조회
    public MemberDTO getProfile(String authorizationHeader) {
        String accessToken = HeaderUtils.extractTokenFromHeader(authorizationHeader);
        Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);

        log.info("회원정보 조회 성공 email: {}", member.getEmail());
        return memberMapper.toDto(member);
    }

    // 로그인 (JWT 발급)
    @Transactional
    public MemberLoginResponseDTO login(MemberLoginDTO memberLoginDTO, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(memberLoginDTO.getEmail())
                .orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FIND_MEMBER));

        String hashedInputPassword = PasswordUtil.hashPassword(memberLoginDTO.getPw(), member.getSalt());
        if (!member.getPw().equals(hashedInputPassword)) {
            throw new GlobalException(GlobalExceptionCode.INVALID_OLD_PASSWORD);
        }

        String accessToken = jwtTokenProvider.generateToken(member.getMemberPk());
        jwtTokenProvider.createRefreshTokenCookie(member.getMemberPk(), response);

        return new MemberLoginResponseDTO(accessToken, member.getEmail(), member.getNickname());
    }

    // 닉네임 변경
    @Transactional
    public NicknameResponseDTO modifyNickname(NicknameRequestDTO nicknameRequest, String accessToken) {
        String newNickname = nicknameRequest.getNewNickname();

        validateNickname(newNickname);

        if (memberRepository.existsByNickname(newNickname)) {
            throw new GlobalException(GlobalExceptionCode.NICKNAME_DUPLICATION);
        }
        Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);
        member = MemberMapper.updateNickname(member, newNickname);
        memberRepository.save(member);

        return new NicknameResponseDTO(newNickname, "닉네임 변경 성공");
    }

    private void validateNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new GlobalException(GlobalExceptionCode.NICKNAME_EMPTY);
        }

        String trimmedNickname = nickname.trim();
        if (trimmedNickname.length() > 8) {
            throw new GlobalException(GlobalExceptionCode.NICKNAME_TOO_LONG);
        }
    }

    @Transactional
    public String updateProfilePicture(MultipartFile file, String accessToken) {

        Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);
        String uniqueImageKey = member.getNickname() + "_" + UUID.randomUUID();
        String uploadedUrl = fileUploader.uploadFile(file, uniqueImageKey);

        if (member.getMemberImage() != null) {
            String existingKey = fileUploader.extractKeyFromUrl(member.getMemberImage());
            fileUploader.deleteImage(existingKey);
        }

        member = member.toBuilder().memberImage(uploadedUrl).build();
        memberRepository.save(member);

        return uploadedUrl;
    }

    //비밀번호 변경
    @Transactional
    public void changePassword(PasswordChangeRequestDTO passwordChangeRequest, String accessToken) {
        Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);
        String newSalt = PasswordUtil.generateSalt();
        String hashedNewPassword = PasswordUtil.hashPassword(passwordChangeRequest.getNewPw(), newSalt);
        member = memberMapper.updatePassword(member, newSalt, hashedNewPassword);
        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(MemberDeleteRequestDTO requestDTO, String accessToken) {
        // 탈퇴 확인 메시지 검증 ㅋ ㅋㅎㅋㅎㅋ
        if (!"오이,, 오이오이오이? 오이.. 오이 ㅠㅠ".equals(requestDTO.getConfirmationMessage())) {
            throw new IllegalArgumentException("탈퇴 확인 메시지가 일치하지 않습니다.");
        }
        Member member = tokenValidator.validateAccessTokenAndReturnMember(accessToken);
        memberRepository.delete(member);
    }

}