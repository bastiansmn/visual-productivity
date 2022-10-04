package com.bastiansmn.vp.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConstant {

    // 10 Days in seconds
    public static final Integer ACCESS_EXPIRATION_TIME = 864_000;
    // 30 minutes in seconds
    public static final Integer REFRESH_EXPIRATION_TIME = 1_800;
    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String ACCESS_TOKEN_URI = "/api/v1";
    public static final String REFRESH_TOKEN_URI = "/api/v1/token/refresh";
    public static final String JWT_SECRET = System.getenv("JWT_SECRET");
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Le token n'a pas pu être vérifié";
    public static final String VP_LLC = "VisualProductivity, LLC";
    public static final String AUTHORITIES = "Authorities";
    public static final String FORBIDDEN_MESSAGE = "Vous devez être identifié pour accéder à cette page";
    public static final String ACCESS_DENIED_MESSAGE = "Vous n'avez pas la permission d'accéder à cette page";
    public static final String USER_NOT_ENABLED_MESSAGE = "Votre compte n'est pas activé";
    public static final String USER_BLOCKED_MESSAGE = "Votre compte est bloqué";
    public static final String INVALID_CREDENTIALS_MESSAGE = "Nom d'utilisateur ou mot de passe invalide";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {
            "/api/v1/login",
            "/api/v1/user/register",
            "/api/v1/token/refresh",
    };

}
