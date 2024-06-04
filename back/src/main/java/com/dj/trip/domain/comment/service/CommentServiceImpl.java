package com.dj.trip.domain.comment.service;


import com.dj.trip.domain.comment.Comment;
import com.dj.trip.domain.comment.CommentInfo;
import com.dj.trip.domain.comment.CommentsDao;
import com.dj.trip.domain.comment.dto.request.CreateCommentRequest;
import com.dj.trip.domain.comment.dto.request.GetCommentsRequest;
import com.dj.trip.domain.comment.dto.request.ModifyCommentRequest;
import com.dj.trip.domain.comment.dto.response.GetCommentsResponse;
import com.dj.trip.domain.comment.mapper.CommentMapper;
import com.dj.trip.domain.image.service.ImageServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final ImageServiceUtils imageServiceUtils;

    @Override
    public void createComment(CreateCommentRequest createCommentRequest, String memberId) {
        Comment comment = Comment.createComment(
                memberId,
                createCommentRequest.reviewId(),
                createCommentRequest.content()
        );

        if (commentMapper.insertComment(comment) == 0) {
            throw new InsufficientAuthenticationException("잘못된 요청");
        }
    }

    @Override
    public GetCommentsResponse getComments(int reviewId, GetCommentsRequest getCommentsRequest, String memberId) {
        CommentsDao commentsDao = CommentsDao.getComments(
                reviewId,
                memberId,
                getCommentsRequest.pagesize(),
                getCommentsRequest.pagesize() * (getCommentsRequest.pageno() - 1)
        );
        List<CommentInfo> comments = commentMapper.selectComments(commentsDao);

        for (CommentInfo comment : comments) {
            if (comment.getImageUrl() != null) {
                comment.setReviewImageUrl(imageServiceUtils.getImageUrl(comment.getImageUrl()));
            }
        }

        int page = getCommentsRequest.pageno();
        int total = commentMapper.getTotalCommentsCount(commentsDao);
        int totalPage = total / getCommentsRequest.pagesize();
        if (total % getCommentsRequest.pagesize() != 0) {
            totalPage++;
        }
        return new GetCommentsResponse(comments, page, totalPage, total);
    }

    @Override
    public void modifyComment(int commentId, ModifyCommentRequest modifyCommentRequest, String memberId) {
        Comment comment = Comment.modifyComment(
                commentId,
                memberId,
                modifyCommentRequest.content()
        );
        if (commentMapper.modifyComment(comment) == 0) {
            throw new InsufficientAuthenticationException("잘못된 요청");
        }
    }

    @Override
    public void deleteComment(int commentId, String memberId) {
        Comment comment = Comment.deleteComment(
                commentId,
                memberId
        );
        if (commentMapper.deleteComment(comment) == 0) {
            throw new InsufficientAuthenticationException("잘못된 요청");
        }
    }
}
