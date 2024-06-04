package com.dj.trip.domain.member.mapper;

import com.dj.trip.domain.member.Member;
import com.dj.trip.domain.member.MemberInfo;
import com.dj.trip.domain.oauth.OauthMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    // 회원가입
    void insertMember(Member member);

    // OauthServiceType과 email로 유저 조회
    Optional<Member> selectMemberByOauthServerTypeAndEmail(OauthMember oauthMember);

    // 아이디로 유저 조회
    Member selectMemberByMemberId(String memberId);

    // 닉네임으로 유저 조회
    Member selectMemberByNickname(String nickname);

    // 이메일로 유저 조회
    Member selectMemberByEmail(String email);

    // 아이디, 이메일로 유저조회
    Member selectMemberByMemberIdAndEmail(String memberId, String email);

    // 유정 정보 수정
    void updateMember(Member member);

    MemberInfo selectMemberInfoByMemberId(String memberId);

    MemberInfo selectMemberPswInfoByMemberId(String member);

    int modifyMember(Member member);

    String getImageUrl(String memberId);

    String getPassword(String memberId);
    int deleteMember(String memberId);
}
