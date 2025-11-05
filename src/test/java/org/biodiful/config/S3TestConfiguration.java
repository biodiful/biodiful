package org.biodiful.config;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Test configuration that provides a mock S3Client for integration tests.
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
}
