package com.bastiansmn.vp.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    private static final String profile = System.getenv("PROFILE");

    public static ResponseCookie generateCookie(String name, String value, String path, Integer maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .path(path)
                .maxAge(maxAge)
                .secure(profile.equals("prod"))
                .sameSite("Strict")
                .build();
    }

    public static void setCookies(HttpServletResponse response, ResponseCookie... cookies) {
        for (ResponseCookie cookie : cookies) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
    }

}
