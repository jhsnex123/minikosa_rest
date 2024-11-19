package com.kosa.mini.api.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileRequest {
    private MemberEditProfileDTO memberEditProfileDTO;
    private PasswordCheckDTO passwordCheckDTO;
}
