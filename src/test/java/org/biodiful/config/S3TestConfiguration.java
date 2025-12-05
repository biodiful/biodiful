package org.biodiful.config;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/**
 * Test configuration that provides mock S3Client and S3Presigner for integration tests.
 * This allows S3Service and S3Resource to be created in test contexts
 * without requiring actual AWS credentials.
 */
@TestConfiguration
public class S3TestConfiguration {

    @Bean
    @Primary
    public S3Client s3Client() {
        return mock(S3Client.class);
    }

    @Bean
    @Primary
    public S3Presigner s3Presigner() {
        return mock(S3Presigner.class);
    }
}
