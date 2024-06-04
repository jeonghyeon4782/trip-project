package com.dj.trip.domain.member.dto.response;

public record GetMemberByPasswordResponse(
        String memberId,
        String nickname,
        String email,
        String imageUrl,
        String password
) {
}
