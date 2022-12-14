package com.bastiansmn.vp.token.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bastiansmn.vp.config.SecurityConstant;
import com.bastiansmn.vp.config.properties.JwtProperties;
import com.bastiansmn.vp.role.RoleDAO;
import com.bastiansmn.vp.token.TokenService;
import com.bastiansmn.vp.user.UserDAO;
import com.bastiansmn.vp.utils.CookieUtils;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtProperties jwtProperties;

    @Override
    public String createAccessToken(String subject, List<String> authorities, Boolean enabled, Boolean notLocked) {
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(Instant.now())
                .withExpiresAt(new Date(Instant.now().toEpochMilli() + jwtProperties.getAccessTokenExpirationTime() * 1000))
                .withIssuer(SecurityConstant.VP_LLC)
                .withClaim("roles", authorities)
                .withClaim("enabled", enabled)
                .withClaim("notLocked", notLocked)
                .sign(Algorithm.HMAC256(jwtProperties.getSecret().getBytes()));
    }

    @Override
    public String createRefreshToken(String subject) {
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(Instant.now())
                .withExpiresAt(new Date(Instant.now().toEpochMilli() + jwtProperties.getRefreshTokenExpirationTime() * 1000))
                .withIssuer(SecurityConstant.VP_LLC)
                .sign(Algorithm.HMAC256(jwtProperties.getSecret().getBytes()));
    }

    @Override
    public void createJWTAndAddInHeaders(UserDAO user, HttpServletResponse response, Boolean secure) {
        String accessToken = this.createAccessToken(
                user.getEmail(),
                user.getRoles().stream()
                        .map(RoleDAO::getName)
                        .collect(Collectors.toList()),
                user.isEnabled(),
                user.isNotLocked()
        );
        String refreshToken = this.createRefreshToken(user.getEmail());

        CookieUtils.setCookies(
                response,
                CookieUtils.generateCookie(
                        SecurityConstant.ACCESS_TOKEN_COOKIE_NAME,
                        accessToken,
                        jwtProperties.getAccessPath(),
                        jwtProperties.getAccessTokenExpirationTime(),
                        secure
                ),
                CookieUtils.generateCookie(
                        SecurityConstant.REFRESH_TOKEN_COOKIE_NAME,
                        refreshToken,
                        jwtProperties.getRefreshPath(),
                        jwtProperties.getRefreshTokenExpirationTime(),
                        secure
                )
        );
    }
}
