package com.kosa.mini.api.service.review;

import com.kosa.mini.api.dto.review.ReviewReplyDTO;
import com.kosa.mini.api.dto.review.ReviewSaveDTO;
import com.kosa.mini.api.dto.review.ReviewResponseDTO;
import com.kosa.mini.api.dto.review.ReviewsUpdateDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Review;
import com.kosa.mini.api.entity.Store;
import com.kosa.mini.api.exception.MemberNotFoundException;
import com.kosa.mini.api.exception.StoreNotFoundException;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.ReviewRepository;
import com.kosa.mini.api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewApiServiceImpl implements ReviewApiService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<ReviewReplyDTO> getReviews(Integer storeId) {
        List<ReviewReplyDTO> reviews = reviewRepository.findByStoreStoreId(storeId);
        return reviews;
    }

    @Override
    @Transactional
    public ReviewResponseDTO createReview(ReviewSaveDTO reviewSaveDTO) {

        Store store = storeRepository.findById(reviewSaveDTO.getStoreId())
                .orElseThrow(() -> new StoreNotFoundException("Store not found with id: " + reviewSaveDTO.getStoreId()));

        Member member = memberRepository.findById(reviewSaveDTO.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + reviewSaveDTO.getMemberId()));

        Review review = reviewSaveDTO.toEntity(store, member);
        Review savedReview = reviewRepository.save(review);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return ReviewResponseDTO.builder()
                .reviewId(savedReview.getReviewId())
                .storeId(savedReview.getStore().getStoreId())
                .memberId(savedReview.getMember().getMemberId())
                .reviewText(savedReview.getReviewText())
                .rating(savedReview.getRating())
                .storeName(savedReview.getStore().getStoreName())
                .memberNickname(savedReview.getMember().getNickname())
                .createdAt(formatter.format(savedReview.getCreatedAt()))
                .updatedAt(savedReview.getUpdatedAt() != null ? formatter.format(savedReview.getUpdatedAt()) : null)
                .build();
    }

    @Override
//    @Transactional
    public Boolean deleteReview(Integer reviewId) {
        if(reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            return true;
        }else {
            return false;
        }
    }

    @Override
    @Transactional
    public ReviewsUpdateDTO updateReviews(ReviewsUpdateDTO reviewsUpdateDTO, Integer memberId, Integer storeId) {
        Review newReview;
        Member member = new Member();
        member.setMemberId(memberId);
        Store store = new Store();
        store.setStoreId(storeId);
        newReview = reviewsUpdateDTO.toEntity(member, store);
        Review oldReview = reviewRepository.findById(reviewsUpdateDTO.getReviewId()).orElse(null);
        if(oldReview == null || reviewsUpdateDTO.getReviewId() == null) {
            return null;
        }
        Review updated = reviewRepository.save(newReview);
        reviewsUpdateDTO.fromEntity(updated);
        return reviewsUpdateDTO;
    }
}
