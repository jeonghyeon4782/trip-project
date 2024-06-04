package com.dj.trip.domain.member.dto.response;

public record GetMemberResponse(
        String memberId,
        String nickname,
        String email,
        String imageUrl,
        Long reviews,
        Long score
) {
}
