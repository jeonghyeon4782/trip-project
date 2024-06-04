package com.dj.trip.domain.oauth.client;

import com.dj.trip.domain.oauth.OauthMember;
import com.dj.trip.domain.oauth.OauthServerType;

public interface OauthMemberClient {
    OauthServerType supportServer();

    OauthMember fetch(String code);
}
