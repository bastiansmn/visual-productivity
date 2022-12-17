package com.bastiansmn.vp.filter;

import com.bastiansmn.vp.config.SecurityConstant;
import com.bastiansmn.vp.config.properties.JwtProperties;
import com.bastiansmn.vp.config.properties.SpringProperties;
import com.bastiansmn.vp.exception.ApiError;
import com.bastiansmn.vp.token.TokenService;
import com.bastiansmn.vp.user.UserPrincipal;
import com.bastiansmn.vp.utils.CookieUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final JwtProperties jwtProperties;
    private final SpringProperties springProperties;
    private final TokenService tokenService;

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
        String accessToken = tokenService.createAccessToken(user.getUsername(), user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()), user.isEnabled(), user.isAccountNonLocked());

        String refreshToken = tokenService.createRefreshToken(user.getUsername());

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
                jwtProperties.getAccessPath(),
                jwtProperties.getAccessTokenExpirationTime(),
                springProperties.getProfile().equals("prod")
            ),
            CookieUtils.generateCookie(
                SecurityConstant.REFRESH_TOKEN_COOKIE_NAME,
                refreshToken,
                jwtProperties.getRefreshPath(),
                jwtProperties.getRefreshTokenExpirationTime(),
                springProperties.getProfile().equals("prod")
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
            ApiError apiError = new ApiError(
                    new Date(),
                    SecurityConstant.USER_NOT_ENABLED_MESSAGE,
                    SecurityConstant.USER_NOT_ENABLED_MESSAGE,
                    HttpStatus.FORBIDDEN,
                    HttpStatus.FORBIDDEN.value()
            );
            new ObjectMapper().writeValue(response.getOutputStream(), apiError);
            return;
        } else if (reason.equals("User account is locked")) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            ApiError apiError = new ApiError(
                    new Date(),
                    SecurityConstant.USER_BLOCKED_MESSAGE,
                    SecurityConstant.USER_BLOCKED_MESSAGE,
                    HttpStatus.FORBIDDEN,
                    HttpStatus.FORBIDDEN.value()
            );
            new ObjectMapper().writeValue(response.getOutputStream(), apiError);
            return;
        }
        // Bad credentials
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        ApiError apiError = new ApiError(
                new Date(),
                SecurityConstant.INVALID_CREDENTIALS_MESSAGE,
                SecurityConstant.INVALID_CREDENTIALS_MESSAGE,
                HttpStatus.FORBIDDEN,
                HttpStatus.FORBIDDEN.value()
        );
        new ObjectMapper().writeValue(response.getOutputStream(), apiError);

        log.error("Authentication failed for user: {} with password: {}", request.getParameter("email"), request.getParameter("password"));
        // TODO Block user after 3 failed attempts and send mail to unlock it
    }

}
