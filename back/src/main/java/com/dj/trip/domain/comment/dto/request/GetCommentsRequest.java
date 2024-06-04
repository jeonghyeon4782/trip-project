package com.dj.trip.domain.comment.dto.request;

public record GetCommentsRequest(
        int pagesize,   // 페이지 크기
        int pageno    // 현재 페이지 번호
) {
}
