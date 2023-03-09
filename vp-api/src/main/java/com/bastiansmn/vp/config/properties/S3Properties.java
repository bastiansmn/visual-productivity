package com.bastiansmn.vp.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "s3", ignoreUnknownFields = false)
@ToString
public class S3Properties {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String tempFolder;
    private String userBucket;
    private String projectBucket;

}
