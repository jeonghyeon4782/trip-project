package com.dj.trip.domain.oauth.service;

import com.dj.trip.domain.member.Member;
import com.dj.trip.domain.member.mapper.MemberMapper;
import com.dj.trip.domain.oauth.OauthMember;
import com.dj.trip.domain.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import com.dj.trip.domain.oauth.client.OauthMemberClientComposite;
import com.dj.trip.domain.oauth.dto.request.OauthLoginRequest;
import com.dj.trip.domain.oauth.dto.response.OauthLoginResponse;
import com.dj.trip.domain.oauth.mapper.OauthMemberMapper;
import com.dj.trip.global.util.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final OauthMemberMapper oauthMemberMapper;
    private final MemberMapper memberMapper;
    private final JWTUtil jwtUtil;

    @Transactional
    public OauthLoginResponse login(OauthLoginRequest oauthLoginRequest, HttpServletResponse response) {
        // 소셜 로그인 성공 후 얻은 OauthMember 정보
        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthLoginRequest.oauthServerType(), oauthLoginRequest.code());

        // 해당 OauthMember가 저장 되어 있지 않으면, 저장을 요청 후 해당 OauthMember 정보를 받아온다.
        OauthMember savedMember = oauthMemberMapper.selectOauthMemberByOauthServerTypeAndEmail(oauthMember);
        if (savedMember == null) {
            oauthMemberMapper.insertOauthMember(oauthMember);
            savedMember = oauthMemberMapper.selectOauthMemberByOauthServerTypeAndEmail(oauthMember);
        }

        // 등록된 유저인지 확인
        Optional<Member> optionalMember = memberMapper.selectMemberByOauthServerTypeAndEmail(savedMember);

        // 등록된 유저가 아니면 회원가입을 위한 iSFirst: true 반환
        if (!optionalMember.isPresent()) {
            return new OauthLoginResponse(savedMember, true);
        }

        // 등록된 유저면, iSFirst: false, 토큰 반환
        Member member = optionalMember.get();
        processUserTokens(member, response);
        return new OauthLoginResponse(savedMember, false);
    }

    private void processUserTokens(Member member, HttpServletResponse response) {
        String accessToken = jwtUtil.getAccessToken(Map.of("memberId", member.getMemberId()));
        String refreshToken = jwtUtil.getRefreshToken(Map.of("memberId", member.getMemberId()));

        jwtUtil.setHeaderAccessToken(response, accessToken);
        jwtUtil.setHeaderRefreshToken(response, refreshToken);
    }
}
