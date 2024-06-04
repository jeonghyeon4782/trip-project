package com.dj.trip.domain.review;

import com.dj.trip.domain.review.dto.response.GetReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetReviewDao {
    private int reviewId;
    private int attractionInfoId;
    private String attractionName;
    private String nickname;
    private String title;
    private String content;
    private String reviewImageUrl;
    private String profileImageUrl;
    private int hits;
    private Timestamp create_date;
    private int likes;
    private boolean isWriteByMe;    // 내가 작성한 글인지
    private boolean isCheckLike; // 좋아요를 눌렸는지

    public void setReviewImageUrl(String reviewImageUrl) {
        this.reviewImageUrl = reviewImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public GetReviewResponse getReviewResponse() {
        return new GetReviewResponse(
                reviewId,
                attractionInfoId,
                attractionName,
                nickname,
                title,
                content,
                reviewImageUrl,
                profileImageUrl,
                hits,
                create_date,
                likes,
                isWriteByMe,    // 내가 작성한 글인지
                isCheckLike
        );
    }
}
