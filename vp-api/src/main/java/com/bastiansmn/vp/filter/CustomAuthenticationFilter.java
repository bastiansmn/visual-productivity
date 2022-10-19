package com.bastiansmn.vp.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bastiansmn.vp.config.SecurityConstant;
import com.bastiansmn.vp.user.UserPrincipal;
import com.bastiansmn.vp.utils.CookieUtils;
import com.bastiansmn.vp.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
            AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    // Generate a token if auth is good
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws
            IOException {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(SecurityConstant.JWT_SECRET.getBytes());
        String accessToken = JwtUtils.createAccessToken(algorithm, user.getUsername(), user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()), user.isEnabled(), user.isAccountNonLocked());

        String refreshToken = JwtUtils.createRefreshToken(algorithm, user.getUsername());

        // If user is not active or locked, send error
        if (!user.isEnabled() || !user.isAccountNonLocked()) {
            Map<String, String> error = new HashMap<>();
            response.setStatus(HttpStatus.FORBIDDEN.value());
            error.put("error", SecurityConstant.ACCESS_DENIED_MESSAGE);
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
            return;
        }

        // Add tokens in cookie
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

        // Put user in response
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), user.getUser());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        String reason = failed.getMessage();
        // TODO: Trouver un autre moyen de savoir pq l'auth est unsuccessful
        if (reason.equals("User is disabled")) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), SecurityConstant.USER_NOT_ENABLED_MESSAGE);
            return;
        } else if (reason.equals("User account is locked")) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), SecurityConstant.USER_BLOCKED_MESSAGE);
            return;
        }
        // Bad credentials
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), SecurityConstant.INVALID_CREDENTIALS_MESSAGE);

        log.error("Authentication failed for user: {} with password: {}", request.getParameter("email"), request.getParameter("password"));
        // TODO Block user after 3 failed attempts and send mail to unlock it
    }

}
