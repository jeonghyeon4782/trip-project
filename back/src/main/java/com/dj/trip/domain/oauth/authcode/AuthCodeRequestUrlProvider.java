package com.dj.trip.domain.oauth.authcode;

import com.dj.trip.domain.oauth.OauthServerType;

public interface AuthCodeRequestUrlProvider {
    OauthServerType supportServer();

    String provide();
}
