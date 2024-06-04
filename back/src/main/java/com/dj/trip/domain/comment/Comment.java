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
public class Comment {
    private int commentId;
    private String memberId;
    private int reviewId;
    private String content;
    private Timestamp create_date;

    public static Comment createComment(String memberId, int reviewId, String content) {
        return builder()
                .memberId(memberId)
                .reviewId(reviewId)
                .content(content)
                .build();
    }

    public static Comment modifyComment(int commentId, String memberId, String content) {
        return builder()
                .commentId(commentId)
                .memberId(memberId)
                .content(content)
                .build();
    }

    public static Comment deleteComment(int commentId, String memberId) {
        return builder()
                .commentId(commentId)
                .memberId(memberId)
                .build();
    }
}
