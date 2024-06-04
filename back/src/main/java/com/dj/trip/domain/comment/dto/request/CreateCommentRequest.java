package com.dj.trip.domain.comment.dto.request;

public record CreateCommentRequest(
        int reviewId,
        String content
) {
}
