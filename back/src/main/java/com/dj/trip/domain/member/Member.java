package com.dj.trip.domain.member;

import com.dj.trip.domain.oauth.OauthServerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    private String memberId;
    private String password;
    private String nickname;
    private String email;
    private String imageUrl;
    private char role;
    private OauthServerType oauthServiceType;

    public static Member getMember(String memberId, String password) {
        return Member.builder()
                .memberId(memberId)
                .password(password)
                .build();
    }

    public static Member modifyMember(String memberId, String nickname, String email, String password, String imageUrl) {
        return Member.builder()
                .memberId(memberId)
                .nickname(nickname)
                .email(email)
                .password(password)
                .imageUrl(imageUrl)
                .build();
    }
}
