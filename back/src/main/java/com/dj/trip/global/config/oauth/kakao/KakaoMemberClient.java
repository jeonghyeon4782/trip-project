package com.dj.trip.global.config.oauth.kakao;

import com.dj.trip.domain.oauth.OauthMember;
import com.dj.trip.domain.oauth.OauthServerType;
import com.dj.trip.domain.oauth.client.OauthMemberClient;
import com.dj.trip.global.config.oauth.kakao.client.KakaoApiClient;
import com.dj.trip.global.config.oauth.kakao.dto.KakaoMemberResponse;
import com.dj.trip.global.config.oauth.kakao.dto.KakaoToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoMemberClient implements OauthMemberClient {

    private final KakaoOauthConfig kakaoOauthConfig;
    private final KakaoApiClient kakaoApiClient;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }

    @Override
    public OauthMember fetch(String authCode) {
        KakaoToken tokenInfo = kakaoApiClient.fetchToken(tokenRequestParams(authCode));
        log.info(tokenInfo.toString());
        KakaoMemberResponse kakaoMemberResponse =
                kakaoApiClient.fetchMember("Bearer " + tokenInfo.accessToken());
        log.info(kakaoMemberResponse.toString());
        return kakaoMemberResponse.toDomain();
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOauthConfig.clientId());
        params.add("redirect_uri", kakaoOauthConfig.redirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOauthConfig.clientSecret());
        return params;
    }
}