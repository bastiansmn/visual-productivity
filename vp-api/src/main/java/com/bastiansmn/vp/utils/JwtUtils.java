package com.bastiansmn.vp.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bastiansmn.vp.config.SecurityConstant;
import com.bastiansmn.vp.role.RoleDAO;
import com.bastiansmn.vp.user.UserDAO;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUtils {

    public static String createAccessToken(Algorithm algorithm, String subject, List<String> authorities, Boolean enabled, Boolean notLocked) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + (SecurityConstant.ACCESS_EXPIRATION_TIME * 1000)))
                .withIssuer(SecurityConstant.VP_LLC)
                .withClaim("roles", authorities)
                .withClaim("enabled", enabled)
                .withClaim("notLocked", notLocked)
                .sign(algorithm);
    }

    public static String createRefreshToken(Algorithm algorithm, String subject) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + (SecurityConstant.REFRESH_EXPIRATION_TIME * 1000)))
                .withIssuer(SecurityConstant.VP_LLC)
                .sign(algorithm);
    }

    public static void createJWTAndAddInHeaders(Algorithm algorithm, UserDAO user, HttpServletResponse response) {
        String accessToken = JwtUtils.createAccessToken(
                algorithm,
                user.getEmail(),
                user.getRoles().stream()
                        .map(RoleDAO::getName)
                        .collect(Collectors.toList()),
                user.isEnabled(),
                user.isNotLocked()
        );
        String refreshToken = JwtUtils.createRefreshToken(
                algorithm,
                user.getEmail()
        );

        CookieUtils.setCookies(
                response,
                CookieUtils.generateCookie(
                        SecurityConstant.ACCESS_TOKEN_COOKIE_NAME,
                        accessToken,
                        SecurityConstant.ACCESS_TOKEN_URI,
                        SecurityConstant.ACCESS_EXPIRATION_TIME
                ),
                CookieUtils.generateCookie(
                        SecurityConstant.REFRESH_TOKEN_COOKIE_NAME,
                        refreshToken,
                        SecurityConstant.REFRESH_TOKEN_URI,
                        SecurityConstant.REFRESH_EXPIRATION_TIME
                )
        );
    }

}
