package com.bastiansmn.vp.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static ResponseCookie generateCookie(String name, String value, String path, Long maxAge, Boolean secure) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .path(path)
                .maxAge(maxAge)
                .secure(secure)
                .build();
    }

    public static void setCookies(HttpServletResponse response, ResponseCookie... cookies) {
        for (ResponseCookie cookie : cookies) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
    }

}
