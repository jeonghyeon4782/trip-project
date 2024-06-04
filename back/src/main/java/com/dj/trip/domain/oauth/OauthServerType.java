package com.dj.trip.domain.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.util.Locale.ENGLISH;

@AllArgsConstructor
@Getter
public enum OauthServerType {

    GOOGLE, KAKAO, GENERAL;

    @JsonCreator
    public static OauthServerType fromName(String type) {
        return OauthServerType.valueOf(type.toUpperCase(ENGLISH));
    }
}
