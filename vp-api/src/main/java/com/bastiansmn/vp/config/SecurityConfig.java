package com.bastiansmn.vp.config;

import com.bastiansmn.vp.config.properties.CorsProperties;
import com.bastiansmn.vp.config.properties.JwtProperties;
import com.bastiansmn.vp.config.properties.SpringProperties;
import com.bastiansmn.vp.filter.CustomAuthenticationFilter;
import com.bastiansmn.vp.filter.CustomAuthorizationFilter;
import com.bastiansmn.vp.token.TokenService;
import com.bastiansmn.vp.token.impl.TokenServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final CorsProperties corsProperties;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        log.info("Allowed origins: {}", corsProperties.getAllowedOrigins());
        configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
        configuration.setAllowedMethods(corsProperties.getAllowedMethods());
        configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
        configuration.setAllowCredentials(Boolean.TRUE);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsProperties.getRegisterPattern(), configuration);
        return source;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), jwtPropertiesBean(), springPropertiesBean(), tokenServiceBean());
        customAuthenticationFilter.setFilterProcessesUrl(SecurityConstant.LOGIN_URI);

        http
                .csrf().disable().cors()
                .and()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
                .and()
                .authorizeRequests().antMatchers(SecurityConstant.PUBLIC_URLS).permitAll()
                .and()
                .formLogin().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(customAuthorizationFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public JwtProperties jwtPropertiesBean() {
        return new JwtProperties();
    }

    @Bean
    public SpringProperties springPropertiesBean() {
        return new SpringProperties();
    }

    @Bean
    public TokenService tokenServiceBean() {
        return new TokenServiceImpl(jwtPropertiesBean());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CustomAuthorizationFilter customAuthorizationFilterBean() {
        return new CustomAuthorizationFilter();
    }

    @Bean
    public FilterRegistrationBean<CustomAuthorizationFilter> myFilterRegistrationBean() {
        FilterRegistrationBean<CustomAuthorizationFilter> frb = new FilterRegistrationBean<>(customAuthorizationFilterBean());
        frb.setEnabled(false);
        return frb;
    }
}
