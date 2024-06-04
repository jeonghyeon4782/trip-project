package com.dj.trip.domain.review.dto.response;

import com.dj.trip.domain.review.ReviewInfo;

import java.util.List;

public record GetReviewsResponse(

        List<ReviewInfo> reviewInfos, // 리뷰 정보들
        int page,    // 현재 위치하는 페이지
        int total
) {

}
