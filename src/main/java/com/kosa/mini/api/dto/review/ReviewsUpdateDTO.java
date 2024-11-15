package com.kosa.mini.api.dto.review;

import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Review;
import com.kosa.mini.api.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewsUpdateDTO {
    private Integer reviewId;
    private Integer storeId;
    private Integer rating;
    private String reviewText;
    private Date updatedAt = new Date();
    private Integer memberId;

    public ReviewsUpdateDTO fromEntity(Review review) {
        return new ReviewsUpdateDTO(
                review.getReviewId(),
                review.getStore().getStoreId(),
                review.getRating(),
                review.getReviewText(),
                review.getUpdatedAt(),
                review.getMember().getMemberId());
    }

    public Review toEntity(Member member, Store store) {
        return Review.builder()
                .reviewId(reviewId)
                .store(store)
                .rating(rating)
                .reviewText(reviewText)
                .updatedAt(updatedAt)
                .member(member)
                .build();
    }

}
