package com.bastiansmn.vp.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt", ignoreUnknownFields = false)
public class JwtProperties {

    private String secret;
    private Long accessTokenExpirationTime;
    private Long refreshTokenExpirationTime;
    private String accessPath;
    private String refreshPath;

}
