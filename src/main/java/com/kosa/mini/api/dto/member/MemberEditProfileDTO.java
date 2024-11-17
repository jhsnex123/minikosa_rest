package com.kosa.mini.api.dto.member;

import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Role;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class MemberEditProfileDTO {
    private Integer memberId;
    private String name;
    private String email;
    private String nickname;
    private String phoneNumber;
    private Role roleId;


    private String password;
    private String confirmPassword;

    private String storeName;


    // DTO -> Entity 변환 메서드
    public Member toEntity() {

        return Member.builder()
                .memberId((memberId))
                .name(name)
                .nickname(nickname)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(roleId)
                .build();
    }

    // Entity -> DTO 변환 메서드 (새로 추가)
    public static MemberEditProfileDTO fromEntity(Member member) {
        MemberEditProfileDTO dto = new MemberEditProfileDTO();
        dto.setMemberId(member.getMemberId());
        dto.setName(member.getName());
        dto.setEmail(member.getEmail());
        dto.setNickname(member.getNickname());
        dto.setPhoneNumber(member.getPhoneNumber());
        dto.setRoleId(member.getRole());
        return dto;
    }

}
