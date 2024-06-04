package com.dj.trip.domain.review.controller;

import com.dj.trip.domain.review.dto.request.CreateReviewRequest;
import com.dj.trip.domain.review.dto.request.GetReviewsRequest;
import com.dj.trip.domain.review.dto.request.ModifyReviewRequest;
import com.dj.trip.domain.review.service.ReviewService;
import com.dj.trip.global.dto.ResponseDto;
import com.dj.trip.global.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final JWTUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ResponseDto<?>> createReview(HttpServletRequest request,
                                                       @RequestPart(value = "review") CreateReviewRequest reviewRequest,
                                                       @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        String memberId = jwtUtil.getMemberIdByToken(request);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new ResponseDto<>(HttpStatus.CREATED.value(), "리뷰 작성 성공",
                reviewService.createReview(reviewRequest, memberId, file)));
    }

    @GetMapping({"{reviewid}"})
    public ResponseEntity<ResponseDto<?>> getReview(HttpServletRequest request,
                                                    @PathVariable("reviewid") int reviewId
    ) {
        String memberId = jwtUtil.getMemberIdByToken(request);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseDto<>(HttpStatus.OK.value(), "리뷰 요청 성공",
                reviewService.getReview(reviewId, memberId)));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<?>> getReviews(@ModelAttribute GetReviewsRequest getReviewsRequest
    ) {
        System.out.println("sido= " + getReviewsRequest.sidos());
        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseDto<>(HttpStatus.OK.value(), "리뷰들 요청 성공",
                reviewService.getReviews(getReviewsRequest)));
    }

    @PutMapping({"{reviewid}"})
    public ResponseEntity<ResponseDto<?>> modifyReview(@PathVariable("reviewid") int reviewId,
                                                       HttpServletRequest request,
                                                       @RequestPart(value = "review") ModifyReviewRequest modigyReviewRequest,
                                                       @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        String memberId = jwtUtil.getMemberIdByToken(request);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new ResponseDto<>(HttpStatus.CREATED.value(), "리뷰 수정 완료",
                reviewService.modifyReview(reviewId, modigyReviewRequest, memberId, file)));
    }

    @DeleteMapping({"{reviewid}"})
    public ResponseEntity<ResponseDto<?>> deleteReview(@PathVariable("reviewid") int reviewId,
                                                       HttpServletRequest request
    ) {
        String memberId = jwtUtil.getMemberIdByToken(request);
        reviewService.deleteReview(reviewId, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
                new ResponseDto<>(HttpStatus.NO_CONTENT.value(), "리뷰 삭제 완료", null));
    }

    @PatchMapping("{reviewid}")
    public ResponseEntity<ResponseDto<?>> updateHits(@PathVariable("reviewid") int reviewId
    ) {
        reviewService.updateHits(reviewId);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
                new ResponseDto<>(HttpStatus.OK.value(), "조회수 증가 완료", null));
    }
}
