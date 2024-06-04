package com.dj.trip.domain.oauth.mapper;

import com.dj.trip.domain.oauth.OauthMember;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OauthMemberMapper {

    //  id로 oauth 유저 조회
    OauthMember selectOauthMemberByOauthId(int oauthId);

    // OauthServiceType과 email로 유저 조회
    OauthMember selectOauthMemberByOauthServerTypeAndEmail(OauthMember oauthMember);

    // oauth 유저 저장
    int insertOauthMember(OauthMember oauthMember);
}
