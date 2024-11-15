package com.kosa.mini.api.repository;

import com.kosa.mini.api.dto.review.ReviewReplyDTO;
import com.kosa.mini.api.dto.review.ReviewsUpdateDTO;
import com.kosa.mini.api.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT new com.kosa.mini.api.dto.review.ReviewReplyDTO(" +
            "r.store.storeId, " +
            "r.member.memberId, " +
            "r.reviewText, " +
            "r.rating, " +
            "r.createdAt, " +
            "rr.replyId, " +
            "r.reviewId, " +
            "rr.owner.memberId, " +
            "rr.replyText, " +
            "r.member.nickname) " +
            "FROM Review r " +
            "LEFT JOIN r.reviewReplies rr " +
            "WHERE r.store.storeId = :storeId " +
            "ORDER BY r.createdAt DESC")
    List<ReviewReplyDTO> findByStoreStoreId(@Param("storeId") Integer storeId);
    ReviewsUpdateDTO findByReviewId(Integer reviewId);
}
