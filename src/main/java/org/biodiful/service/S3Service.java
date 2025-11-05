package org.biodiful.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

/**
 * Service for interacting with AWS S3 to list and manage image files.
 */
@Service
public class S3Service {

    private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp");

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Lists all image files in an S3 folder given its URL.
     *
     * @param folderUrl the S3 folder URL (e.g., https://bucket-name.s3.region.provider/folder/)
     * @return list of full S3 URLs to image files
     * @throws IllegalArgumentException if the folder URL is invalid
     * @throws S3Exception if there's an error communicating with S3
     */
    public List<String> listImages(String folderUrl) {
        LOG.debug("Listing images from S3 folder: {}", folderUrl);

        // Parse the folder URL to extract bucket and prefix
        S3FolderInfo folderInfo = parseS3FolderUrl(folderUrl);

        try {
            // Build the ListObjectsV2 request
            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(folderInfo.bucketName)
                .prefix(folderInfo.prefix)
                .build();

            // Execute the request
            ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

            // Filter for image files and build full URLs
            List<String> imageUrls = new ArrayList<>();
            for (S3Object s3Object : listResponse.contents()) {
                String key = s3Object.key();
                if (isImageFile(key)) {
                    String imageUrl = buildImageUrl(folderInfo.bucketName, folderInfo.region, folderInfo.providerDomain, key);
                    imageUrls.add(imageUrl);
                }
            }

            LOG.debug("Found {} images in S3 folder: {}", imageUrls.size(), folderUrl);
            return imageUrls;
        } catch (S3Exception e) {
            LOG.error("Error listing S3 objects in bucket {} with prefix {}: {}", folderInfo.bucketName, folderInfo.prefix, e.getMessage());
            throw new RuntimeException("Failed to list images from S3: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    /**
     * Parses an S3 folder URL to extract bucket name, region, provider domain, and prefix.
     *
     * @param folderUrl the S3 folder URL
     * @return S3FolderInfo containing bucket, region, provider domain, and prefix
     * @throws IllegalArgumentException if the URL format is invalid
     */
    private S3FolderInfo parseS3FolderUrl(String folderUrl) {
        try {
            URL url = new URL(folderUrl);
            String host = url.getHost();

            // Parse bucket name, region, and provider domain from host
            // Expected formats:
            // - bucket-name.s3.region.amazonaws.com
            // - bucket-name.s3.region.scw.cloud
            // - bucket-name.s3.provider.com (without explicit region)
            String[] hostParts = host.split("\\.");

            if (hostParts.length < 4 || !host.contains(".s3.")) {
                throw new IllegalArgumentException("Invalid S3 URL format: " + folderUrl);
            }

            String bucketName = hostParts[0];
            String region;
            String providerDomain;

            // Determine region and provider domain from host
            if (hostParts.length >= 5) {
                // Format: bucket.s3.region.provider.tld (e.g., bucket.s3.fr-par.scw.cloud or bucket.s3.eu-west-1.amazonaws.com)
                region = hostParts[2];
                // Provider domain is everything after "s3.region."
                providerDomain = String.join(".", Arrays.copyOfRange(hostParts, 3, hostParts.length));
            } else {
                // Format: bucket.s3.provider.tld (no explicit region, use default)
                region = "us-east-1"; // Default region when not specified
                // Provider domain is everything after "s3."
                providerDomain = String.join(".", Arrays.copyOfRange(hostParts, 2, hostParts.length));
            }

            // Extract prefix from path (remove leading slash)
            String prefix = url.getPath();
            if (prefix.startsWith("/")) {
                prefix = prefix.substring(1);
            }

            return new S3FolderInfo(bucketName, region, providerDomain, prefix);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid S3 folder URL: " + folderUrl, e);
        }
    }

    /**
     * Checks if a file key represents an image file based on its extension.
     *
     * @param key the S3 object key
     * @return true if the file is an image
     */
    private boolean isImageFile(String key) {
        String lowerKey = key.toLowerCase();
        return IMAGE_EXTENSIONS.stream().anyMatch(lowerKey::endsWith);
    }

    /**
     * Builds a full S3 URL for an object.
     *
     * @param bucketName the bucket name
     * @param region the region
     * @param providerDomain the S3 provider domain (e.g., amazonaws.com, scw.cloud)
     * @param key the object key
     * @return the full S3 URL
     */
    private String buildImageUrl(String bucketName, String region, String providerDomain, String key) {
        return String.format("https://%s.s3.%s.%s/%s", bucketName, region, providerDomain, key);
    }

    /**
     * Internal class to hold S3 folder information.
     */
    private static class S3FolderInfo {

        final String bucketName;
        final String region;
        final String providerDomain;
        final String prefix;

        S3FolderInfo(String bucketName, String region, String providerDomain, String prefix) {
            this.bucketName = bucketName;
            this.region = region;
            this.providerDomain = providerDomain;
            this.prefix = prefix;
        }
    }
}
