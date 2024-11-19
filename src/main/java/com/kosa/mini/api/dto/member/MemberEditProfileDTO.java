package com.kosa.mini.api.dto.member;

import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Role;
import com.kosa.mini.api.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberEditProfileDTO {
    private Integer memberId;
    private String name;
    private String email;
    private String nickname;
    private String phoneNumber;
    private Integer roleId;
    //
//
//    private String password;
//    private String confirmPassword;

    private String storeName;
//
//    public MemberEditProfileDTO(Integer memberId, String name, String email, String nickname, String phoneNumber, Integer roleId, String storeName) {
//        this.memberId = memberId;
//        this.name = name;
//        this.email = email;
//        this.nickname = nickname;
//        this.phoneNumber = phoneNumber;
//        this.roleId = roleId;
//        this.storeName = storeName;
//    }

    // DTO -> Entity 변환 메서드
    public Member toEntity(Role role) {
        return Member.builder()
                .memberId((memberId))
                .name(name)
                .nickname(nickname)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(role)
                .build();
    }
//
//    // Entity -> DTO 변환 메서드 (새로 추가) 수정페이지니 set을써야 하고 엔티티()에 있는 객체들을 받아와서 수정
//    public MemberEditProfileDTO fromEntity(Member member, Store store) {
//        return new MemberEditProfileDTO(
//                member.getMemberId(),
//                member.getName(),
//                member.getEmail(),
//                member.getNickname(),
//                member.getPhoneNumber(),
//                member.getRole().getRoleId(),
//                store.getStoreName());
//    }
}

//    public void fromEntity(Member member) {
//        this.memberId = member.getMemberId();
//        this.name = member.getName();
//        this.email = member.getEmail();
//        this.nickname = member.getNickname();
//        this.phoneNumber = member.getPhoneNumber();
//        this.roleId = member.getRoleId(); // Role ID를 가져옴
//    }


