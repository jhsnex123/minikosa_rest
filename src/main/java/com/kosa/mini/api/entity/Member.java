package com.kosa.mini.api.entity;

import com.kosa.mini.api.dto.member.MemberEditProfileDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "nickname", nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 관계 매핑: 회원이 소유한 가게들
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Store> stores;

    // 관계 매핑: 회원이 작성한 리뷰들
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviews;

    // 관계 매핑: 회원이 작성한 리뷰 답글들
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ReviewReply> reviewReplies;

    // 관계 매핑: 회원이 제출한 제안들
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ContactUs> contactUsList;

    public Integer getRoleId() {
        return this.role != null ? this.role.getRoleId() : null;
    }

//    public void put(Member member) {
//        if(member.name != null)
//            this.name= member.name;
//        if(member.nickname != null)
//            this.nickname= member.nickname;
//        if(member.phoneNumber != null)
//            this.phoneNumber= member.phoneNumber;
//        if(member.password != null)
//            this.password=member.password;
//        if(member.role != null)
//            this.role=member.role;
//    }
}
