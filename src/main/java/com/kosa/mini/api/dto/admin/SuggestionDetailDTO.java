package com.kosa.mini.api.dto.admin;

import lombok.*;
import com.kosa.mini.api.entity.ContactUs;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuggestionDetailDTO {
    private String title;
    private String storeName;
    private String content;
    private Integer memberId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isModified;
    private Integer views;
    private String memberName;
    private Integer contactId;

    public SuggestionDetailDTO fromEntity(ContactUs contactUs) {
        return SuggestionDetailDTO.builder()
                .contactId(contactUs.getContactId())
                .title(contactUs.getTitle())
                .storeName(contactUs.getStoreName())
                .createdAt(contactUs.getCreatedAt())
                .updatedAt(contactUs.getUpdatedAt())
                .isModified(contactUs.getIsModified())
                .content(contactUs.getContent())
                .views(contactUs.getViews())
                .build();
    }
}
