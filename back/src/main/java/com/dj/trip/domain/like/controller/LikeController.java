package com.dj.trip.domain.like.controller;

import com.dj.trip.domain.like.service.LikeService;
import com.dj.trip.global.dto.ResponseDto;
import com.dj.trip.global.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final JWTUtil jwtUtil;

    @PostMapping("{reviewid}")
    public ResponseDto<?> createLike(@PathVariable("reviewid") int reviewId,
                                     HttpServletRequest request) {
        String memberId = jwtUtil.getMemberIdByToken(request);
        likeService.createLike(reviewId, memberId);
        return new ResponseDto<>(HttpStatus.CREATED.value(), "좋아요 성공", null);
    }

    @DeleteMapping("{reviewid}")
    public ResponseDto<?> deleteLike(@PathVariable("reviewid") int reviewId,
                                     HttpServletRequest request) {
        String memberId = jwtUtil.getMemberIdByToken(request);
        likeService.deleteLike(reviewId, memberId);
        return new ResponseDto<>(HttpStatus.NO_CONTENT.value(), "좋아요 취소", null);
    }


}
