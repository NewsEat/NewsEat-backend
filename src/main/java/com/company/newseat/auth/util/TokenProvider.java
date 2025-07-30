package com.company.newseat.auth.util;

import com.company.newseat.global.config.JwtProperties;
import com.company.newseat.auth.dto.jwt.JwtUserDetails;
import com.company.newseat.auth.dto.jwt.Tokens;
import com.company.newseat.auth.type.JwtValidationResult;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT 생성, 파싱, 유효성 검증, 사용자 정보 추출
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {
    private static final String ROLE = "role";

    private final JwtProperties jwtProperties;

    private Key key;

    @PostConstruct
    public void setKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());
            this.key = Keys.hmacShaKeyFor(keyBytes);
            log.info("JWT Signing Key initialized successfully : {}", key);
        } catch (Exception e) {
            log.error("Failed to initialize JWT key", e);
            throw new IllegalStateException("JWT key initialization failed");
        }
    }

    /**
     * 액세스토큰, 리프레시 토큰 생성
     */
    public Tokens generateToken(JwtUserDetails jwtUserDetails) {
        return new Tokens(createToken(jwtUserDetails, jwtProperties.accessTokenExpiration()),
                createToken(jwtUserDetails, jwtProperties.refreshTokenExpiration()));
    }

    /**
     * JWT 생성
     */
    public String createToken(JwtUserDetails jwtUserDetails, Long expireTime) {
        Date now = new Date();

        Claims claims = Jwts.claims()
                .setSubject(jwtUserDetails.userId().toString())
                .setIssuer(jwtProperties.issuer())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireTime));

        claims.put(ROLE, jwtUserDetails.role());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(key)
                .compact();
    }

    /**
     * 토큰 유효성 검사
     */
    public JwtValidationResult validateToken(String token) {
        try {
            if (getClaims(token).getExpiration().after(new Date())) {
                return JwtValidationResult.VALID_JWT;
            } else {
                return JwtValidationResult.EXPIRED_JWT;
            }
        } catch (MalformedJwtException ex) {
            return JwtValidationResult.INVALID_JWT;
        } catch (ExpiredJwtException ex) {
            return JwtValidationResult.EXPIRED_JWT;
        } catch (UnsupportedJwtException ex) {
            return JwtValidationResult.UNSUPPORTED_JWT;
        } catch (IllegalArgumentException ex) {
            return JwtValidationResult.EMPTY_JWT;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public JwtUserDetails getJwtUserDetails(String token) {
        return JwtUserDetails.fromClaims(getClaims(token));
    }
}
