package com.bastiansmn.vp.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("cors")
public class CorsProperties {

    List<String> allowedOrigins;
    List<String> allowedMethods;
    List<String> allowedHeaders;
    String registerPattern;

}
