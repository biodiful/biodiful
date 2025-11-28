package org.biodiful.web.rest;

import jakarta.annotation.security.PermitAll;
import java.util.List;
import org.biodiful.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for S3 operations.
 * Provides endpoints to list media files (images, videos, audio) from S3 buckets.
 */
@RestController
@RequestMapping("/api/s3")
public class S3Resource {

    private static final Logger LOG = LoggerFactory.getLogger(S3Resource.class);

    private final S3Service s3Service;

    public S3Resource(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    /**
     * {@code GET  /s3/list-media} : List all media files in an S3 folder.
     * <p>
     * This endpoint is publicly accessible to allow survey participants to view media files.
     * Supports images, videos, and audio files.
     *
     * @param folderUrl the S3 folder URL (e.g., https://bucket-name.s3.region.provider/folder/)
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and list of media URLs in body,
     *         or with status {@code 400 (Bad Request)} if the folder URL is invalid,
     *         or with status {@code 500 (Internal Server Error)} if there's an error accessing S3.
     */
    @GetMapping("/list-media")
    @PermitAll
    public ResponseEntity<S3MediaListResponse> listMedia(@RequestParam("folderUrl") String folderUrl) {
        LOG.debug("REST request to list S3 media files from folder: {}", folderUrl);

        try {
            List<String> mediaUrls = s3Service.listMediaFiles(folderUrl);

            if (mediaUrls.isEmpty()) {
                LOG.warn("No media files found in S3 folder: {}", folderUrl);
            }

            return ResponseEntity.ok(new S3MediaListResponse(mediaUrls));
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid S3 folder URL: {}", folderUrl, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            LOG.error("Error listing S3 media files from folder: {}", folderUrl, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Response DTO for S3 media list endpoint.
     */
    public static class S3MediaListResponse {

        private List<String> mediaUrls;

        public S3MediaListResponse() {}

        public S3MediaListResponse(List<String> mediaUrls) {
            this.mediaUrls = mediaUrls;
        }

        public List<String> getMediaUrls() {
            return mediaUrls;
        }

        public void setMediaUrls(List<String> mediaUrls) {
            this.mediaUrls = mediaUrls;
        }
    }
}
