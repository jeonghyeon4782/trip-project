package com.dj.trip.global.config.oauth.google;

import com.dj.trip.domain.oauth.OauthMember;
import com.dj.trip.domain.oauth.OauthServerType;
import com.dj.trip.domain.oauth.client.OauthMemberClient;
import com.dj.trip.global.config.oauth.google.client.GoogleApiClient;
import com.dj.trip.global.config.oauth.google.dto.GoogleMemberResponse;
import com.dj.trip.global.config.oauth.google.dto.GoogleToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class GoogleMemberClient implements OauthMemberClient {

    private final GoogleOauthConfig googleOauthConfig;
    private final GoogleApiClient googleApiClient;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.GOOGLE;
    }

    @Override
    public OauthMember fetch(String authCode) {
        String code = URLDecoder.decode(authCode, StandardCharsets.UTF_8);
        GoogleToken tokenInfo = googleApiClient.fetchToken(tokenRequestParams(code));
        GoogleMemberResponse googleMemberResponse = googleApiClient.fetchMember("Bearer " + tokenInfo.accessToken());
        return googleMemberResponse.toDomain();
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authCode);
        params.add("client_id", googleOauthConfig.clientId());
        params.add("client_secret", googleOauthConfig.clientSecret());
        params.add("redirect_uri", googleOauthConfig.redirectUri());
        params.add("grant_type", "authorization_code");
        return params;
    }
}
