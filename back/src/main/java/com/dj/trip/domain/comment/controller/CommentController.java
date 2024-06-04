package com.dj.trip.domain.comment.controller;

import com.dj.trip.domain.comment.dto.request.CreateCommentRequest;
import com.dj.trip.domain.comment.dto.request.GetCommentsRequest;
import com.dj.trip.domain.comment.dto.request.ModifyCommentRequest;
import com.dj.trip.domain.comment.service.CommentService;
import com.dj.trip.global.dto.ResponseDto;
import com.dj.trip.global.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JWTUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ResponseDto<?>> createComment(HttpServletRequest request,
                                                        @RequestBody CreateCommentRequest createCommentRequest
    ) {
        String memberId = jwtUtil.getMemberIdByToken(request);
        commentService.createComment(createCommentRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
                new ResponseDto<>(HttpStatus.CREATED.value(), "댓글 작성 성공", null));
    }

    @GetMapping({"{reviewid}"})
    public ResponseEntity<ResponseDto<?>> getComments(@PathVariable("reviewid") int reviewId,
                                                      HttpServletRequest request,
                                                      @ModelAttribute GetCommentsRequest getCommentsRequest
    ) {
        String memberId = jwtUtil.getMemberIdByToken(request);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
                new ResponseDto<>(HttpStatus.OK.value(), "댓글 목록 요청 성공",
                        commentService.getComments(reviewId, getCommentsRequest, memberId)));
    }

    @PutMapping({"{commentid}"})
    public ResponseEntity<ResponseDto<?>> modifyReview(@PathVariable("commentid") int commentId,
                                                       HttpServletRequest request,
                                                       @RequestBody ModifyCommentRequest modifyCommentRequest
    ) {
        String memberId = jwtUtil.getMemberIdByToken(request);
        commentService.modifyComment(commentId, modifyCommentRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
                new ResponseDto<>(HttpStatus.CREATED.value(), "댓글 수정 완료", null));
    }

    @DeleteMapping({"{commentid}"})
    public ResponseEntity<ResponseDto<?>> deleteReview(@PathVariable("commentid") int commentId,
                                                       HttpServletRequest request
    ) {
        String memberId = jwtUtil.getMemberIdByToken(request);
        commentService.deleteComment(commentId, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
                new ResponseDto<>(HttpStatus.NO_CONTENT.value(), "댓글 삭제 완료", null));
    }
}
