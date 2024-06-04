package com.dj.trip.domain.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    private int reviewId;
    private int attractionInfoId;
    private String memberId;
    private String title;
    private String content;
    private int hits;
    private String imageUrl;
    private Timestamp create_date;

    public static Review createReview(int attractionInfoId, String memberId, String title, String content, String imageUrl) {
        return builder()
                .attractionInfoId(attractionInfoId)
                .memberId(memberId)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .build();
    }

    public static Review getReview(int reviewId, String memberId) {
        return builder()
                .reviewId(reviewId)
                .memberId(memberId)
                .build();
    }

    public static Review modifyReview(int reviewId, int attractionInfoId, String memberId, String title, String content, String imageUrl) {
        return builder()
                .reviewId(reviewId)
                .attractionInfoId(attractionInfoId)
                .memberId(memberId)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .build();
    }

    public static Review deleteReview(int reviewId, String memberId) {
        return builder()
                .reviewId(reviewId)
                .memberId(memberId)
                .build();
    }

    public static Review updateLikes(int reviewId, String memberId) {
        return builder()
                .reviewId(reviewId)
                .memberId(memberId)
                .build();
    }
}
