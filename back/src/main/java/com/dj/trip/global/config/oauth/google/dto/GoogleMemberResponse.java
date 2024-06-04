package com.dj.trip.global.config.oauth.google.dto;

import com.dj.trip.domain.oauth.OauthMember;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import static com.dj.trip.domain.oauth.OauthServerType.GOOGLE;

@JsonNaming(value = SnakeCaseStrategy.class)
public record GoogleMemberResponse(
        String id,
        String email,
        String name,
        String picture
) {
    public OauthMember toDomain() {
        return OauthMember.builder()
                .email(email)
                .oauthServerType(GOOGLE)
                .build();
    }
}
