package com.OEzoa.OEasy.application.member;

import org.springframework.stereotype.Component;

@Component
public class SignUpValidator {

    private static final String NICKNAME_PATTERN = "^[a-zA-Z0-9가-힣]{1,8}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";

    public boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.length() <= 50;
    }

    public boolean isValidNickname(String nickname) {
        return nickname != null && nickname.matches(NICKNAME_PATTERN);
    }

    public boolean isValidPassword(String password) {
        return password != null && password.matches(PASSWORD_PATTERN);
    }
}