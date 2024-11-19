package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.MemberEditProfileDTO;
import com.kosa.mini.api.dto.member.PasswordCheckDTO;
import com.kosa.mini.api.entity.Member;

public interface MemberEditProfileService {
    boolean isNicknameExists(String nickname);

    MemberEditProfileDTO getUserInfo(Integer memberId);

    Member update(Integer memberId, MemberEditProfileDTO memberEditProfileDTO, PasswordCheckDTO passwordCheckDTO);
}

