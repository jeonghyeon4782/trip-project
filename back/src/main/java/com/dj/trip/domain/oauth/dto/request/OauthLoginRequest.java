package com.dj.trip.domain.oauth.dto.request;

import com.dj.trip.domain.oauth.OauthServerType;

public record OauthLoginRequest(
        OauthServerType oauthServerType,
        String code
) {
}
