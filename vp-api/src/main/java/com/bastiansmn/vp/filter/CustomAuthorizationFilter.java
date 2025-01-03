package com.bastiansmn.vp.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bastiansmn.vp.config.SecurityConstant;
import com.bastiansmn.vp.config.properties.JwtProperties;
import com.bastiansmn.vp.exception.ApiError;
import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        // Page doesn't requires authentication
        if (Arrays.stream(SecurityConstant.PUBLIC_URLS).toList().contains(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        // No cookie found in request
        Cookie authorizationHeaderCookie = WebUtils.getCookie(request, SecurityConstant.ACCESS_TOKEN_COOKIE_NAME);
        log.debug("Authorization header cookie: {}", authorizationHeaderCookie);
        if (authorizationHeaderCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeaderCookie.getValue();
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret().getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String email = decodedJWT.getSubject();
            if (!this.userService.isEnabled(email))
                throw new FunctionalException(
                        FunctionalRule.USER_0006,
                        FORBIDDEN
                );

            if (!this.userService.isNotLocked(email))
                throw new FunctionalException(
                        FunctionalRule.USER_0005,
                        FORBIDDEN
                );

            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
            Collection<SimpleGrantedAuthority> authorities =
                    stream(roles)
                            .map(SimpleGrantedAuthority::new)
                            .toList();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (FunctionalException e) {
            log.error("{}: {}", SecurityConstant.ACCESS_DENIED_MESSAGE, e.getMessage());
            response.setStatus(e.getHttpStatus().value());
            response.setContentType(APPLICATION_JSON_VALUE);
            var apiError = new ApiError(
                    new Date(),
                    e.getClientMessage(),
                    e.getMessage(),
                    e.getHttpStatus(),
                    e.getHttpStatus().value()
            );
            new ObjectMapper().writeValue(response.getOutputStream(), apiError);
        } catch (TokenExpiredException exception) {
            log.error("{}: {}", SecurityConstant.TOKEN_EXPIRED, exception.getMessage());
            response.setStatus(FORBIDDEN.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            var apiError = new ApiError(
                    new Date(),
                    SecurityConstant.TOKEN_EXPIRED,
                    SecurityConstant.TOKEN_EXPIRED,
                    FORBIDDEN,
                    FORBIDDEN.value()
            );
            new ObjectMapper().writeValue(response.getOutputStream(), apiError);
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error(
                    "{} ({}): {}",
                    SecurityConstant.TOKEN_CANNOT_BE_VERIFIED,
                    token,
                    exception.getMessage()
            );
            response.setStatus(FORBIDDEN.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            var apiError = new ApiError(
                    new Date(),
                    SecurityConstant.TOKEN_CANNOT_BE_VERIFIED,
                    exception.getMessage(),
                    FORBIDDEN,
                    FORBIDDEN.value()
            );
            new ObjectMapper().writeValue(response.getOutputStream(), apiError);
        }
    }

}
