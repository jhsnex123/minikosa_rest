package com.kosa.mini.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "review_replies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Integer replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

    @Column(name = "reply_text", nullable = false, columnDefinition = "TEXT")
    private String replyText;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "is_modified")
    private Boolean isModified;

    // 엔티티가 업데이트되기 전에 호출되는 메서드
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
        isModified = true;
    }
}
