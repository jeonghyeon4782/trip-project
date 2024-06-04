package com.dj.trip.domain.review.dto.request;

public record CreateReviewRequest(
        int attractionInfoId,
        String attractionName,
        String title,
        String content
) {
}
