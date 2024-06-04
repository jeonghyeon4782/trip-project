package com.dj.trip.domain.review.dto.request;

public record ModifyReviewRequest(
        int attractionInfoId,
        String title,
        String content
) {
}
