package com.dj.trip.domain.review.service;

import com.dj.trip.domain.image.service.ImageServiceUtils;
import com.dj.trip.domain.review.GetReviewDao;
import com.dj.trip.domain.review.Review;
import com.dj.trip.domain.review.ReviewInfo;
import com.dj.trip.domain.review.ReviewsDao;
import com.dj.trip.domain.review.dto.request.CreateReviewRequest;
import com.dj.trip.domain.review.dto.request.GetReviewsRequest;
import com.dj.trip.domain.review.dto.request.ModifyReviewRequest;
import com.dj.trip.domain.review.dto.response.CreateReviewResponse;
import com.dj.trip.domain.review.dto.response.GetReviewResponse;
import com.dj.trip.domain.review.dto.response.GetReviewsResponse;
import com.dj.trip.domain.review.dto.response.ModifyReviewResponse;
import com.dj.trip.domain.review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final ImageServiceUtils imageServiceUtils;

    @Override
    @Transactional
    public CreateReviewResponse createReview(CreateReviewRequest reviewRequest,
                                             String memberId,
                                             MultipartFile file
    ) {
        String fileName = null;
        if (file != null) {
            fileName = imageServiceUtils.upload(file);
        }

        Review review = Review
                .createReview(
                        reviewRequest.attractionInfoId(),
                        memberId,
                        reviewRequest.title(),
                        reviewRequest.content(),
                        fileName
                );

        if (reviewMapper.insertReview(review) == 0) {
            throw new InsufficientAuthenticationException("잘못된 요청");
        }
        int id = reviewMapper.selectReviewId(review);
        return new CreateReviewResponse(id);
    }

    @Override
    public GetReviewResponse getReview(int reviewId, String memberId) {
        Review review = Review
                .getReview(
                        reviewId,
                        memberId
                );

        GetReviewDao getReviewDao = reviewMapper.selectReview(review);
        if (getReviewDao != null) {
            if (getReviewDao.getReviewImageUrl() != null) {
                getReviewDao.setReviewImageUrl(imageServiceUtils.getImageUrl(getReviewDao.getReviewImageUrl()));
            }
            if (getReviewDao.getProfileImageUrl() != null) {
                getReviewDao.setProfileImageUrl(imageServiceUtils.getImageUrl(getReviewDao.getProfileImageUrl()));
            }
            return getReviewDao.getReviewResponse();
        }
        return null;
    }

    @Override
    public GetReviewsResponse getReviews(GetReviewsRequest getReviewsRequest) {
        ReviewsDao reviewsDao = ReviewsDao
                .getReviews(
                        getReviewsRequest.keyword(),
                        getReviewsRequest.sidos(),
                        getReviewsRequest.order(),
                        getReviewsRequest.pagesize(),
                        (getReviewsRequest.pageno() - 1) * getReviewsRequest.pagesize()
                );
        List<ReviewInfo> reviews = reviewMapper.selectReviews(reviewsDao);

        for (ReviewInfo review : reviews) {
            if (review.getReviewImageUrl() != null) {
                review.setReviewImageUrl(imageServiceUtils.getImageUrl(review.getReviewImageUrl()));
            }
            if (review.getProfileImageUrl() != null) {
                review.setProfileImageUrl(imageServiceUtils.getImageUrl(review.getProfileImageUrl()));
            }
        }

        int page = getReviewsRequest.pageno();
        int total = reviewMapper.getTotalReviewsCount(reviewsDao);
        int totalPage = total / getReviewsRequest.pagesize();
        if (total % getReviewsRequest.pagesize() != 0) {
            totalPage++;
        }
        return new GetReviewsResponse(reviews, page, totalPage);
    }

    @Override
    @Transactional
    public ModifyReviewResponse modifyReview(int reviewId,
                                             ModifyReviewRequest modigyReviewRequest,
                                             String memberId,
                                             MultipartFile file) {

        String fileName = null;
        if (file != null) {
            fileName = imageServiceUtils.upload(file);
        }
        Review review = Review
                .modifyReview(
                        reviewId,
                        modigyReviewRequest.attractionInfoId(),
                        memberId,
                        modigyReviewRequest.title(),
                        modigyReviewRequest.content(),
                        fileName
                );

        if (fileName != null) {
            // 해당 id의 이미지 이름으로 삭제 요청
            String image_url = reviewMapper.getImageUrl(review);
            if (image_url != null) {
                imageServiceUtils.deleteImage(image_url);
            }
        }

        if (reviewMapper.modifyReview(review) == 0) {
            throw new InsufficientAuthenticationException("잘못된 요청");
        }
        return new ModifyReviewResponse(review.getReviewId());
    }

    @Override
    public void deleteReview(int reviewId, String memberId) {

        Review review = Review
                .deleteReview(
                        reviewId,
                        memberId
                );

        // 해당 id의 이미지 이름으로 삭제 요청
        String image_url = reviewMapper.getImageUrl(review);
        if (image_url != null) {
            imageServiceUtils.deleteImage(image_url);
        }

        if (reviewMapper.deleteReview(review) == 0) {
            throw new InsufficientAuthenticationException("잘못된 요청");
        }
    }

    @Override
    public void updateHits(int reviewId) {
        if (reviewMapper.updateHit(reviewId) == 0) {
            throw new InsufficientAuthenticationException("잘못된 요청");
        }
    }
}
