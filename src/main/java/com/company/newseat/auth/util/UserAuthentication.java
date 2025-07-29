package com.company.newseat.auth.util;

import com.company.newseat.auth.dto.jwt.JwtUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * JWT에서 받은 사용자 정보를 스프링 시큐리티 인증 객체로 변환
 */
public class UserAuthentication extends UsernamePasswordAuthenticationToken {

    public UserAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static UserAuthentication from(JwtUserDetails jwtUserDetails) {
        return new UserAuthentication(jwtUserDetails.userId(), "", jwtUserDetails.role().getAuthority());
    }
}
