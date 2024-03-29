package com.bastiansmn.vp.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConstant {

    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String LOGIN_URI = "/login";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Le token n'a pas pu être vérifié";
    public static final String TOKEN_EXPIRED = "Le token a expiré";
    public static final String VP_LLC = "VisualProductivity, LLC";
    public static final String AUTHORITIES = "Authorities";
    public static final String FORBIDDEN_MESSAGE = "Vous devez être identifié pour accéder à cette page";
    public static final String ACCESS_DENIED_MESSAGE = "Vous n'avez pas la permission d'accéder à cette page";
    public static final String USER_NOT_ENABLED_MESSAGE = "Votre compte n'est pas activé";
    public static final String USER_BLOCKED_MESSAGE = "Votre compte est bloqué";
    public static final String INVALID_CREDENTIALS_MESSAGE = "Email ou mot de passe invalide";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String LOGIN_IS_SOCIAL = "Votre compte est lié à un compte social (Google), connectez vous avec celui ci.";
    public static final String[] PUBLIC_URLS = {
            "/login",
            "/user/register",
            "/token/refresh",
            "/token/validate",
            "/mail/confirm",
            "/mail/revalidate",
            "/oauth2/login",
            "/",
            "/docs/**",
            "/swagger-ui/**",
            "/actuator/**",
    };

}
