package com.kosa.mini.api.service.review;

import com.kosa.mini.api.dto.review.*;

import java.util.List;

public interface ReviewApiService {
    List<ReviewReplyDTO> getReviews(Integer storeId);
    ReviewResponseDTO createReview(ReviewSaveDTO dto);
    Boolean deleteReview(Integer reviewId);
    ReviewsUpdateDTO updateReviews(ReviewsUpdateDTO reviewsUpdateDTO, Integer memberId, Integer storeId);
}
