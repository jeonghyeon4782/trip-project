package com.dj.trip.domain.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentInfo {
    private int commentId;
    private String nickname;
    private String imageUrl;
    private String content;
    private Timestamp createDate;
    private boolean isWriteByMe;

    public void setReviewImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
