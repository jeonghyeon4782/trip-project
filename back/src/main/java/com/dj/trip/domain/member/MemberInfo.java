package com.dj.trip.domain.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfo {
    private String memberId;
    private String nickname;
    private String email;
    private String password;
    private String imageUrl;
    private Long reviews;
    private Long score;
}
