package com.dj.trip.domain.review;

import lombok.Builder;

import java.util.List;

@Builder
public record ReviewsDao(
        String keyword, // 검색 단어
        List<Integer> sidos,   // 지역
        String order,   // 정렬 기준
        int pageSize,   // 페이지 크기
        int start    // 시작 위치
) {
    public static ReviewsDao getReviews(String keyword, List<Integer> sidos, String order, int pageSize, int start) {
        return builder()
                .keyword(keyword)
                .sidos(sidos)
                .order(order)
                .pageSize(pageSize)
                .start(start)
                .build();
    }
}
