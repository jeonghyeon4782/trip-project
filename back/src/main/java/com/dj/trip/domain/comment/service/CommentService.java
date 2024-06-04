package com.dj.trip.domain.comment.service;

import com.dj.trip.domain.comment.dto.request.CreateCommentRequest;
import com.dj.trip.domain.comment.dto.request.GetCommentsRequest;
import com.dj.trip.domain.comment.dto.request.ModifyCommentRequest;
import com.dj.trip.domain.comment.dto.response.GetCommentsResponse;

public interface CommentService {
    void createComment(CreateCommentRequest createCommentRequest, String memberId);

    GetCommentsResponse getComments(int reviewId, GetCommentsRequest getCommentsRequest, String memberId);

    void modifyComment(int commentId, ModifyCommentRequest modifyCommentRequest, String memberId);

    void deleteComment(int commentId, String memberId);
}
