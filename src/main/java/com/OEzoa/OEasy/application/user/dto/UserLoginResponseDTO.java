package com.OEzoa.OEasy.application.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserLoginResponseDTO {
    private String accessToken;
    private String email;
    private String nickname;
}
