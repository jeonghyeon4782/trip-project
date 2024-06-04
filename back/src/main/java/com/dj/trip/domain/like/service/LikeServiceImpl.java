package com.dj.trip.domain.like.service;

import com.dj.trip.domain.like.Like;
import com.dj.trip.domain.like.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeMapper likeMapper;

    @Override
    public void createLike(int reviewId, String memberId) {
        Like like = Like.builder()
                .memberId(memberId)
                .reviewId(reviewId)
                .build();
        if (likeMapper.isHasLike(like) || likeMapper.isWriteByMe(like)) {
            throw new InsufficientAuthenticationException("잘못된 요청");
        }
        likeMapper.insertLike(like);
    }

    @Override
    public void deleteLike(int reviewId, String memberId) {
        Like like = Like.builder()
                .memberId(memberId)
                .reviewId(reviewId)
                .build();
        if (likeMapper.deleteLike(like) == 0) {
            throw new InsufficientAuthenticationException("잘못된 요청");
        }
    }
}
