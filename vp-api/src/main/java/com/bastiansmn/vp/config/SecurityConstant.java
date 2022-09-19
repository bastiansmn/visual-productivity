package com.bastiansmn.vp.config;

import org.springframework.http.HttpHeaders;

public class SecurityConstant {

    public static final long EXPIRATION_TIME = 864_000_000; // 10 Days in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = HttpHeaders.AUTHORIZATION;
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Le token n'a pas pu être vérifié";
    public static final String VP_LLC = "VisualProductivity, LLC";
    public static final String AUTHORITIES = "Authorities";
    public static final String FORBIDDEN_MESSAGE = "Vous devez être identifié pour accéder à cette page";
    public static final String ACCESS_DENIED_MESSAGE = "Vous n'avez pas la permission d'accéder à cette page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {
            "/api/v1/login",
            "/api/v1/user/register",
            "/api/v1/token/refresh",
    };

}
