package com.dj.trip.domain.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewInfo {
    private int reviewId;
    private String nickname;
    private String title;
    private String reviewImageUrl;
    private String profileImageUrl;
    private int hits;
    private int likes;

    public void setReviewImageUrl(String reviewImageUrl) {
        this.reviewImageUrl = reviewImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
