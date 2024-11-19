package com.kosa.mini.api.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordCheckDTO {
    private String password;
    private String confirmPassword;

    // 비밀번호 검증 메서드 추가
    public boolean isPasswordValid() {
        return password != null && password.equals(confirmPassword);
    }
}
