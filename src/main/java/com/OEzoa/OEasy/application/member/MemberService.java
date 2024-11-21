package com.OEzoa.OEasy.application.member;

import com.OEzoa.OEasy.application.member.dto.MemberDeleteRequestDTO;
import com.OEzoa.OEasy.application.member.dto.MemberLoginDTO;
import com.OEzoa.OEasy.application.member.dto.MemberLoginResponseDTO;
import com.OEzoa.OEasy.application.member.dto.MemberSignUpDTO;
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
import com.OEzoa.OEasy.util.JwtTokenProvider;
import com.OEzoa.OEasy.util.PasswordUtil;
import com.OEzoa.OEasy.util.s3Bucket.FileUploader;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
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

    // 일반 회원 가입
    @Transactional
    public void registerMember(MemberSignUpDTO memberSignUpDTO) throws Exception {
        if (memberRepository.findByEmail(memberSignUpDTO.getEmail()).isPresent()) {
            throw new GlobalException(GlobalExceptionCode.EMAIL_DUPLICATION);
        }
        if (memberRepository.existsByNickname(memberSignUpDTO.getNickname())) {
            throw new GlobalException(GlobalExceptionCode.NICKNAME_DUPLICATION);
        }
        if (memberSignUpDTO.getPw() == null || memberSignUpDTO.getPw().isEmpty()) {
            throw new GlobalException(GlobalExceptionCode.INVALID_PASSWORD);
        }
        // 비밀번호 해싱
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(memberSignUpDTO.getPw(), salt);

        Member member = Member.builder()
                .email(memberSignUpDTO.getEmail())
                .pw(hashedPassword)
                .salt(salt)
                .nickname(memberSignUpDTO.getNickname())
                .build();
        memberRepository.save(member);
        log.info("신규 회원 저장 완료: " + member);
    }

    // 일반 로그인 처리 (JWT 발급)
    @Transactional
    public MemberLoginResponseDTO login(MemberLoginDTO memberLoginDTO, HttpSession session) {

        Member member = memberRepository.findByEmail(memberLoginDTO.getEmail())
                .orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FIND_MEMBER));

        String hashedInputPassword = PasswordUtil.hashPassword(memberLoginDTO.getPw(), member.getSalt());
        if (!member.getPw().equals(hashedInputPassword)) {
            throw new GlobalException(GlobalExceptionCode.INVALID_OLD_PASSWORD);
        }

        String jwtAccessToken = jwtTokenProvider.generateToken(member.getMemberPk());
        String jwtRefreshToken = jwtTokenProvider.generateRefreshToken(member.getMemberPk());
        session.setAttribute("accessToken", jwtAccessToken);
        session.setAttribute("refreshToken", jwtRefreshToken);
        log.info("로그인 성공. 생성된 액세스 토큰: " + jwtAccessToken);
        log.info("로그인 성공. 생성된 리프레시 토큰: " + jwtRefreshToken);

        // MemberToken 저장 & 업데이트
        MemberToken memberToken = memberTokenRepository.findById(member.getMemberPk()).orElse(null);
        if (memberToken == null) {
            // 신규 MemberToken 생성
            memberToken = MemberToken.builder()
                    .member(member)
                    .accessToken(jwtAccessToken)
                    .refreshToken(jwtRefreshToken)
                    .build();
            log.info("신규 MemberToken 생성: " + memberToken);
        } else {
            // 기존 MemberToken 업데이트
            memberToken = memberToken.toBuilder()
                    .accessToken(jwtAccessToken)
                    .refreshToken(jwtRefreshToken)
                    .build();
            log.info("기존 MemberToken 업데이트: " + memberToken);
        }
        memberToken = memberTokenRepository.save(memberToken);
        log.info("MemberToken 저장 완료: " + memberToken);

        // AccessToken, RefreshToken 반환
        return MemberLoginResponseDTO.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
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
        member = member.toBuilder().pw(hashedNewPassword).salt(newSalt).build();
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