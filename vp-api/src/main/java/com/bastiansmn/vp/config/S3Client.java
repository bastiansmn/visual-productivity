package com.bastiansmn.vp.config;

import com.bastiansmn.vp.config.properties.S3Properties;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@RequiredArgsConstructor
public class S3Client {

    private final S3Properties s3Properties;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(s3Properties.getEndpoint())
                .credentials(s3Properties.getAccessKey(), s3Properties.getSecretKey())
                .build();
    }

}
