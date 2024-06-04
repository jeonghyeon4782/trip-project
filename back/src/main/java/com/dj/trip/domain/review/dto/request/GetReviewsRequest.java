package com.dj.trip.domain.review.dto.request;

import java.util.List;

public record GetReviewsRequest(
        String keyword,   // 검색 단어
        List<Integer> sidos, // 지역
        String order,   // 정렬 기준
        int pagesize,   // 페이지 크기
        int pageno    // 현재 페이지 번호
) {
}
