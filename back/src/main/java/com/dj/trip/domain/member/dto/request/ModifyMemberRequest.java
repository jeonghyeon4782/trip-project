package com.dj.trip.domain.member.dto.request;

public record ModifyMemberRequest(
        String nickname,
        String email,
        String password
) {
}
