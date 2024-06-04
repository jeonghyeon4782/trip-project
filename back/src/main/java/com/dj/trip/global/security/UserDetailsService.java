package com.dj.trip.global.security;

import com.dj.trip.domain.member.Member;
import com.dj.trip.domain.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("------------------------------------------UserDetailsService------------------------------------------");

        Member member = memberMapper.selectMemberByMemberId(username);

        MemberDto dto = new MemberDto(
                member.getMemberId(),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        return dto;
    }
}
