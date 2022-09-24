package com.bastiansmn.vp.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bastiansmn.vp.config.SecurityConstant;
import com.bastiansmn.vp.user.UserDAO;
import com.bastiansmn.vp.user.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter  {

    private final String profile = System.getenv("PROFILE");

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
            AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    // Generate a token if auth is good
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws
            IOException {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(SecurityConstant.JWT_SECRET.getBytes());
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.ACCESS_EXPIRATION_TIME))
                .withIssuer(SecurityConstant.VP_LLC)
                .withClaim("roles",
                        user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .withClaim("enabled", user.isEnabled())
                .withClaim("notLocked", user.isAccountNonLocked())
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(SecurityConstant.VP_LLC)
                .sign(algorithm);

        // If user is not active or locked, send error
        if (!user.isEnabled() || !user.isAccountNonLocked()) {
            Map<String, String> error = new HashMap<>();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            error.put("error", SecurityConstant.ACCESS_DENIED_MESSAGE);
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
            return;
        }

        // Add tokens in cookie
        ResponseCookie jwtAccessCookie = this.generateCookie(
                SecurityConstant.ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                SecurityConstant.ACCESS_TOKEN_URI,
                SecurityConstant.ACCESS_EXPIRATION_TIME
        );
        ResponseCookie jwtRefreshCookie = this.generateCookie(
                SecurityConstant.REFRESH_TOKEN_COOKIE_NAME,
                refreshToken,
                SecurityConstant.REFRESH_TOKEN_URI,
                SecurityConstant.REFRESH_EXPIRATION_TIME
        );
        response.addHeader(HttpHeaders.SET_COOKIE, jwtAccessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString());

        // Put user in response
        Map<String, UserDAO> body = new HashMap<>();
        body.put("user", user.getUser());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }

//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//
//    }

    private ResponseCookie generateCookie(String name, String value, String path, Long maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .path(path)
                .maxAge(maxAge)
                .secure(profile.equals("prod"))
                .build();
    }
}
