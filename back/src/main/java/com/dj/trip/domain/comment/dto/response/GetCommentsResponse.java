package com.dj.trip.domain.comment.dto.response;

import com.dj.trip.domain.comment.CommentInfo;

import java.util.List;

public record GetCommentsResponse(
        List<CommentInfo> comments,
        int page,
        int pageTotal,
        int total
) {
}
