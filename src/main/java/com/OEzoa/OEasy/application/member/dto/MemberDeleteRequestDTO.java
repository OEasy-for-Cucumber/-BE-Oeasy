package com.OEzoa.OEasy.application.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberDeleteRequestDTO {
    @NotEmpty(message = "탈퇴 확인 메시지를 입력해주세요.")
    private String confirmationMessage;
}
