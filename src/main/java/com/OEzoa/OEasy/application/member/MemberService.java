package com.OEzoa.OEasy.application.member;

import com.OEzoa.OEasy.application.member.dto.MemberDTO;
import com.OEzoa.OEasy.application.member.dto.MemberLoginDTO;
import com.OEzoa.OEasy.application.member.dto.MemberLoginResponseDTO;
import com.OEzoa.OEasy.application.member.dto.MemberSignUpDTO;
import com.OEzoa.OEasy.application.member.dto.NicknameRequestDTO;
import com.OEzoa.OEasy.application.member.dto.NicknameResponseDTO;
import com.OEzoa.OEasy.application.member.mapper.MemberMapper;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.domain.member.MemberToken;
import com.OEzoa.OEasy.domain.member.MemberTokenRepository;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.util.JwtTokenProvider;
import com.OEzoa.OEasy.util.PasswordUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    // 일반 회원 가입
    public void registerMember(MemberSignUpDTO memberSignUpDTO) throws Exception {
        if (memberRepository.findByEmail(memberSignUpDTO.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }
        if (memberSignUpDTO.getPw() == null || memberSignUpDTO.getPw().isEmpty()) {
            throw new Exception("비밀번호는 필수 입력 항목입니다.");
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

    public MemberDTO registerMember(MemberDTO memberDTO, String rawPassword) {
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(rawPassword, salt);

        Member member = MemberMapper.toEntity(memberDTO).toBuilder()
                .pw(hashedPassword)
                .salt(salt)
                .build();

        // Member 엔티티를 저장
        Member savedMember = memberRepository.save(member);
        return MemberMapper.toDto(savedMember);
    }

    // 일반 로그인 처리 (JWT 발급)
    public MemberLoginResponseDTO login(MemberLoginDTO memberLoginDTO, HttpSession session) throws Exception {
        Member member = memberRepository.findByEmail(memberLoginDTO.getEmail())
                .orElseThrow(() -> new Exception("해당 이메일의 회원이 존재하지 않습니다."));

        String hashedInputPassword = PasswordUtil.hashPassword(memberLoginDTO.getPw(), member.getSalt());

        if (!member.getPw().equals(hashedInputPassword)) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
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
    public NicknameResponseDTO modifyNickname(NicknameRequestDTO nicknameRequest, String accessToken) {
        String newNickname = nicknameRequest.getNewNickname();

        validateNickname(newNickname);
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


}