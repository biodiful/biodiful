package org.biodiful.config;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/**
 * Configuration for S3-compatible client (AWS S3, Scaleway, etc.).
 * Credentials and endpoint are configured via application properties.
 */
@Configuration
public class S3Configuration {

    private static final Logger LOG = LoggerFactory.getLogger(S3Configuration.class);

    private final ApplicationProperties applicationProperties;

    public S3Configuration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    /**
     * Creates an S3Client bean if S3 credentials are configured.
     * Supports any S3-compatible provider by specifying a custom endpoint.
     * Credentials can be provided via:
     * - application.yml properties (application.s3.access-key-id, application.s3.secret-access-key, application.s3.endpoint)
     * - Environment variables mapped to properties
     *
     * @return configured S3Client
     */
    @Bean
    @ConditionalOnProperty(prefix = "application.s3", name = { "access-key-id", "secret-access-key" })
    public S3Client s3Client() {
        ApplicationProperties.S3 s3Config = applicationProperties.getS3();

        LOG.info("Initializing S3Client with region: {} and endpoint: {}", s3Config.getRegion(), s3Config.getEndpoint());

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(s3Config.getAccessKeyId(), s3Config.getSecretAccessKey());

        software.amazon.awssdk.services.s3.S3ClientBuilder clientBuilder = S3Client.builder()
            .region(Region.of(s3Config.getRegion()))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials));

        // If a custom endpoint is specified, configure it (for non-AWS S3-compatible services)
        if (s3Config.getEndpoint() != null && !s3Config.getEndpoint().isEmpty()) {
            LOG.info("Using custom S3 endpoint: {}", s3Config.getEndpoint());
            clientBuilder
                .endpointOverride(URI.create(s3Config.getEndpoint()))
                .serviceConfiguration(
                    software.amazon.awssdk.services.s3.S3Configuration.builder()
                        .pathStyleAccessEnabled(true) // Required for most S3-compatible services
                        .build()
                );
        }

        return clientBuilder.build();
    }

    /**
     * Creates an S3Presigner bean for generating presigned URLs.
     * Uses the same configuration as the S3Client.
     *
     * @return configured S3Presigner
     */
    @Bean
    @ConditionalOnProperty(prefix = "application.s3", name = { "access-key-id", "secret-access-key" })
    public S3Presigner s3Presigner() {
        ApplicationProperties.S3 s3Config = applicationProperties.getS3();

        LOG.info("Initializing S3Presigner with region: {} and endpoint: {}", s3Config.getRegion(), s3Config.getEndpoint());

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(s3Config.getAccessKeyId(), s3Config.getSecretAccessKey());

        S3Presigner.Builder presignerBuilder = S3Presigner.builder()
            .region(Region.of(s3Config.getRegion()))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials));

        // If a custom endpoint is specified, configure it (for non-AWS S3-compatible services)
        if (s3Config.getEndpoint() != null && !s3Config.getEndpoint().isEmpty()) {
            LOG.info("Using custom S3 endpoint for presigner: {}", s3Config.getEndpoint());
            presignerBuilder.endpointOverride(URI.create(s3Config.getEndpoint()));
        }

        return presignerBuilder.build();
    }
}
