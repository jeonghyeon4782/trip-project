package com.dj.trip.domain.review.mapper;

import com.dj.trip.domain.review.GetReviewDao;
import com.dj.trip.domain.review.Review;
import com.dj.trip.domain.review.ReviewInfo;
import com.dj.trip.domain.review.ReviewsDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    int insertReview(Review review);

    int selectReviewId(Review review);

    GetReviewDao selectReview(Review review);

    List<ReviewInfo> selectReviews(ReviewsDao reviewsDao);

    int getTotalReviewsCount(ReviewsDao reviewsDao);

    String getImageUrl(Review review);

    int modifyReview(Review review);

    int deleteReview(Review review);

    int updateHit(int reviewId);
}
