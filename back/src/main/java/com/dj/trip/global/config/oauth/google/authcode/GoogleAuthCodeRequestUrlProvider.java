package com.dj.trip.global.config.oauth.google.authcode;

import com.dj.trip.domain.oauth.OauthServerType;
import com.dj.trip.domain.oauth.authcode.AuthCodeRequestUrlProvider;
import com.dj.trip.global.config.oauth.google.GoogleOauthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GoogleAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {
    private final GoogleOauthConfig googleOauthConfig;

    public OauthServerType supportServer() {
        return OauthServerType.GOOGLE;
    }

    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", googleOauthConfig.clientId())
                .queryParam("redirect_uri", googleOauthConfig.redirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", googleOauthConfig.scope())
                .build()
                .toUriString();
    }
}
