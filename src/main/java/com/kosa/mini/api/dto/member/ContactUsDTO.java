package com.kosa.mini.api.dto.member;


import com.kosa.mini.api.entity.ContactUs;
import com.kosa.mini.api.entity.Member;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContactUsDTO {
    private String title;
    private String storeName;
    private String content;
    private Integer memberId;
    private LocalDateTime createdAt;
    private Boolean isModified;
    private Integer views;

    // DTO -> Entity 변환 메서드
    public ContactUs toEntity(Member member) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content must not be null or empty");
        }

        return ContactUs.builder()
                .title(title)
                .storeName(storeName)
                .content(content)
                .member(member)
                .createdAt(LocalDateTime.now())
                .isModified(false)
                .views(0)
                .build();
    }

}
