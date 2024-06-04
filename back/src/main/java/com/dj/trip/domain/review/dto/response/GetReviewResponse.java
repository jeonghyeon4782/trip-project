package com.dj.trip.domain.review.dto.response;

import java.sql.Timestamp;

public record GetReviewResponse(
        int reviewId,
        int attractionInfoId,
        String attractionName,
        String nickname,
        String title,
        String content,
        String reviewImageUrl,
        String profileImageUrl,
        int hits,
        Timestamp create_date,
        int likes,
        boolean isWriteByMe,    // 내가 작성한 글인지
        boolean isCheckLike // 좋아요를 눌렸는지
) {
}
