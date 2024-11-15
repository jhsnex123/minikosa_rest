package com.kosa.mini.api.dto.review;

import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Review;
import com.kosa.mini.api.entity.ReviewReply;
import com.kosa.mini.api.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyUpdateDTO {
    private Integer replyId;
    private Integer reviewId;
    private String replyText;
    private Date updatedAt = new Date();

    public ReplyUpdateDTO fromEntity(ReviewReply reviewReply) {
        return new ReplyUpdateDTO(
                reviewReply.getReplyId(),
                reviewReply.getReview().getReviewId(),
                reviewReply.getReplyText(),
                reviewReply.getUpdatedAt());
    }

    public ReviewReply toEntity(Member member, Review review) {
        return ReviewReply.builder()
                .replyId(replyId)
                .owner(member)
                .review(review)
                .replyText(replyText)
                .updatedAt(updatedAt)
                .build();
    }

}
