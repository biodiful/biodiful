package org.biodiful.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

/**
 * Service for interacting with S3-compatible storage to list and manage media files.
 * Supports images, videos, and audio files.
 */
@Service
public class S3Service {

    private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp");
    private static final List<String> VIDEO_EXTENSIONS = Arrays.asList(".mp4", ".webm", ".mov", ".avi");
    private static final List<String> AUDIO_EXTENSIONS = Arrays.asList(".mp3", ".wav", ".ogg", ".m4a");

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public S3Service(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    /**
     * Lists all media files (images, videos, audio) in an S3 folder given its URL.
     *
     * @param folderUrl the S3 folder URL (e.g., https://bucket-name.s3.region.provider/folder/)
     * @return list of full S3 URLs to media files
     * @throws IllegalArgumentException if the folder URL is invalid
     * @throws S3Exception if there's an error communicating with S3
     */
    public List<String> listMediaFiles(String folderUrl) {
        LOG.debug("Listing media files from S3 folder: {}", folderUrl);

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

            // Filter for media files (images, videos, audio) and build presigned URLs
            List<String> mediaUrls = new ArrayList<>();
            for (S3Object s3Object : listResponse.contents()) {
                String key = s3Object.key();
                if (isMediaFile(key)) {
                    String presignedUrl = generatePresignedUrl(folderInfo.bucketName, key);
                    mediaUrls.add(presignedUrl);
                }
            }

            LOG.debug("Found {} media files in S3 folder: {}", mediaUrls.size(), folderUrl);
            return mediaUrls;
        } catch (S3Exception e) {
            LOG.error("Error listing S3 objects in bucket {} with prefix {}: {}", folderInfo.bucketName, folderInfo.prefix, e.getMessage());
            throw new RuntimeException("Failed to list media files from S3: " + e.awsErrorDetails().errorMessage(), e);
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
     * Checks if a file key represents a media file (image, video, or audio) based on its extension.
     *
     * @param key the S3 object key
     * @return true if the file is a supported media type
     */
    private boolean isMediaFile(String key) {
        return isImageFile(key) || isVideoFile(key) || isAudioFile(key);
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
     * Checks if a file key represents a video file based on its extension.
     *
     * @param key the S3 object key
     * @return true if the file is a video
     */
    private boolean isVideoFile(String key) {
        String lowerKey = key.toLowerCase();
        return VIDEO_EXTENSIONS.stream().anyMatch(lowerKey::endsWith);
    }

    /**
     * Checks if a file key represents an audio file based on its extension.
     *
     * @param key the S3 object key
     * @return true if the file is audio
     */
    private boolean isAudioFile(String key) {
        String lowerKey = key.toLowerCase();
        return AUDIO_EXTENSIONS.stream().anyMatch(lowerKey::endsWith);
    }

    /**
     * Generates a presigned URL for an S3 object that is valid for 1 hour.
     *
     * @param bucketName the bucket name
     * @param key the object key
     * @return the presigned URL valid for 1 hour
     */
    private String generatePresignedUrl(String bucketName, String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1))
                .getObjectRequest(getObjectRequest)
                .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            return presignedRequest.url().toString();
        } catch (Exception e) {
            LOG.error("Error generating presigned URL for {}/{}: {}", bucketName, key, e.getMessage());
            throw new RuntimeException("Failed to generate presigned URL", e);
        }
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
