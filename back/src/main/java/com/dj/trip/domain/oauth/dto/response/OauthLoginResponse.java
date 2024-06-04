package com.dj.trip.domain.oauth.dto.response;

import com.dj.trip.domain.oauth.OauthMember;
import com.dj.trip.domain.oauth.OauthServerType;

public record OauthLoginResponse(
        String email,
        OauthServerType oauthServerType,
        Boolean isFirst
) {
    public OauthLoginResponse(OauthMember oauthMember, Boolean status) {
        this(oauthMember.getEmail(), oauthMember.getOauthServerType(), status);
    }
}
