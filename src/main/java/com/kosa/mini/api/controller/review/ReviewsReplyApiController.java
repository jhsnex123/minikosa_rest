package com.kosa.mini.api.controller.review;

import com.kosa.mini.api.dto.review.*;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Review;
import com.kosa.mini.api.entity.ReviewReply;
import com.kosa.mini.api.exception.MemberNotFoundException;
import com.kosa.mini.api.exception.StoreNotFoundException;
import com.kosa.mini.api.service.review.ReplyApiService;
import com.kosa.mini.api.service.review.ReviewApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewsReplyApiController {

    private final ReviewApiService reviewApiService;
    private final ReplyApiService replyApiService;

    
    // 리뷰 작성
    @PostMapping("/review/{storeId}")
    public ResponseEntity<ReviewResponseDTO> createReview(
            @PathVariable("storeId") Integer storeId,
            @Valid @RequestBody ReviewSaveDTO reviewSaveDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // UserDetails에서 username (memberId) 가져오기
        String memberIdStr = userDetails.getUsername(); // CustomUserDetailsService에서 username을 memberId로 설정했음
        Integer memberId;
        try {
            memberId = Integer.parseInt(memberIdStr);
        } catch (NumberFormatException e) {
            log.error("Invalid memberId format: {}", memberIdStr);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // ReviewSaveDTO에 memberId 및 storeId 설정
        reviewSaveDTO.setMemberId(memberId);
        reviewSaveDTO.setStoreId(storeId);

        try {
            ReviewResponseDTO created = reviewApiService.createReview(reviewSaveDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (StoreNotFoundException | MemberNotFoundException e) {
            log.error("리뷰 생성 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("리뷰 생성 중 예상치 못한 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 리뷰 조회
    @GetMapping("/reviews/{storeId}")
    public ResponseEntity<?> getReviews(@PathVariable("storeId") Integer storeId,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // UserDetails에서 username (memberId) 가져오기
        String memberIdStr = userDetails.getUsername(); // CustomUserDetailsService에서 username을 memberId로 설정했음
        Integer memberId;
        try {
            memberId = Integer.parseInt(memberIdStr);
        } catch (NumberFormatException e) {
            log.error("Invalid memberId format: {}", memberIdStr);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            return ResponseEntity.ok(reviewApiService.getReviews(storeId));
        } catch (Exception e) {
            log.error("리뷰 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 조회 중 오류가 발생했습니다.");
        }
    }

    // 리뷰 삭제
    @Transactional
    @DeleteMapping("/reviews/{storeId}/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Integer reviewId, @PathVariable Integer storeId) {
        try {
            boolean reviewDelete = reviewApiService.deleteReview(reviewId);
            if (reviewDelete) {
                return ResponseEntity.status(HttpStatus.OK).body("댓글이 정상적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("댓글이 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("댓글 삭제 중 문제가 발생하였습니다.");
        }
    }

    // 리뷰 수정
    @PatchMapping("/reviews/{storeId}/{reviewId}")
    public ResponseEntity<ReviewsUpdateDTO> updateReview(@PathVariable Integer reviewId,
                                                         @PathVariable Integer storeId,
                                                         @RequestBody ReviewsUpdateDTO reviewsUpdateDTO,
                                                         @AuthenticationPrincipal UserDetails userDetails) {

        String memberIdStr = userDetails.getUsername();
        Integer memberId;
        memberId = Integer.parseInt(memberIdStr);
        System.out.println("컨트롤러 : " + memberId + "========" + reviewsUpdateDTO.toString());
        reviewsUpdateDTO = reviewApiService.updateReviews(reviewsUpdateDTO, memberId, storeId);
        System.out.println("컨트롤러2 : " + reviewsUpdateDTO.toString());
        if(reviewsUpdateDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(reviewsUpdateDTO);
        }
    }

    // 답글 작성
    @PostMapping("/reply/{reviewId}")
    public ResponseEntity<ReplySaveDTO> createReviewsReply(@PathVariable Integer reviewId,
                                                           @RequestBody ReplySaveDTO replySaveDTO,
                                                           @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // UserDetails에서 username (memberId) 가져오기
        String ownerIdStr = userDetails.getUsername(); // CustomUserDetailsService에서 username을 memberId로 설정했음
        Integer ownerId;
        try {
            ownerId = Integer.parseInt(ownerIdStr);
        } catch (NumberFormatException e) {
            log.error("Invalid memberId format: {}", ownerIdStr);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            ReplySaveDTO created = replyApiService.createReview(replySaveDTO, ownerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(replySaveDTO);
        } catch (StoreNotFoundException | MemberNotFoundException e) {
            log.error("리뷰 생성 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("리뷰 생성 중 예상치 못한 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 답글 수정
    @PatchMapping("/reply/{reviewId}/{replyId}")
    public ResponseEntity<ReplyUpdateDTO> updateReply(@PathVariable Integer reviewId,
                                                      @PathVariable Integer replyId,
                                                      @RequestBody ReplyUpdateDTO replyUpdateDTO,
                                                      @AuthenticationPrincipal UserDetails userDetails) {

        String ownerIdStr = userDetails.getUsername();
        Integer ownerId;
        ownerId = Integer.parseInt(ownerIdStr);
        System.out.println("컨트롤러 : " + ownerId + "========" + replyUpdateDTO.toString());
        replyUpdateDTO = replyApiService.updateReply(replyUpdateDTO, ownerId, reviewId);
        System.out.println("컨트롤러2 : " + replyUpdateDTO.toString());
        if(replyUpdateDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(replyUpdateDTO);
        }
    }


    // 답글 삭제
    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity<String> deleteReply(@PathVariable Integer replyId) {
        try {
            boolean replyDelete = replyApiService.deleteReply(replyId);
            if (replyDelete) {
                return ResponseEntity.status(HttpStatus.OK).body("답글이 정상적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 중 문제가 발생하였습니다.");
        }
    }
}
