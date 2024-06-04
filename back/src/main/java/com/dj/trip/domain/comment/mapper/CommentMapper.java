package com.dj.trip.domain.comment.mapper;

import com.dj.trip.domain.comment.Comment;
import com.dj.trip.domain.comment.CommentInfo;
import com.dj.trip.domain.comment.CommentsDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    int insertComment(Comment comment);

    List<CommentInfo> selectComments(CommentsDao commentsDao);

    int getTotalCommentsCount(CommentsDao commentsDao);

    int modifyComment(Comment comment);

    int deleteComment(Comment comment);
}
