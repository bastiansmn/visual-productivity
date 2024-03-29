package com.bastiansmn.vp.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SpringProperties {

    @Value("${spring.profiles.active}")
    private String profile;

}
