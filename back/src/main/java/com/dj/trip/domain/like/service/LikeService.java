package com.dj.trip.domain.like.service;

public interface LikeService {
    void createLike(int reviewId, String memberId);

    void deleteLike(int reviewId, String memberId);
}
