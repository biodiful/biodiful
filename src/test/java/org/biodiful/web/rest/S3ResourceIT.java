package org.biodiful.web.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import org.biodiful.IntegrationTest;
import org.biodiful.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link S3Resource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class S3ResourceIT {

    @Autowired
    private MockMvc restS3MockMvc;

    @MockitoBean
    private S3Service s3Service;

    @BeforeEach
    void resetMocks() {
        org.mockito.Mockito.reset(s3Service);
    }

    @Test
    void shouldListImagesFromS3FolderWithAWSProvider() throws Exception {
        // Given
        String folderUrl = "https://test-bucket.s3.eu-central-1.amazonaws.com/test-folder/";
        when(s3Service.listImages(anyString())).thenReturn(
            Arrays.asList(
                "https://test-bucket.s3.eu-central-1.amazonaws.com/test-folder/image1.jpg",
                "https://test-bucket.s3.eu-central-1.amazonaws.com/test-folder/image2.png"
            )
        );

        // When/Then
        restS3MockMvc
            .perform(get("/api/s3/list-images").param("folderUrl", folderUrl).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.imageUrls").isArray())
            .andExpect(jsonPath("$.imageUrls", hasSize(2)))
            .andExpect(jsonPath("$.imageUrls[0]").value("https://test-bucket.s3.eu-central-1.amazonaws.com/test-folder/image1.jpg"))
            .andExpect(jsonPath("$.imageUrls[1]").value("https://test-bucket.s3.eu-central-1.amazonaws.com/test-folder/image2.png"));
    }

    @Test
    void shouldListImagesFromS3FolderWithScalewayProvider() throws Exception {
        // Given
        String folderUrl = "https://test-bucket.s3.fr-par.scw.cloud/test-folder/";
        when(s3Service.listImages(anyString())).thenReturn(
            Arrays.asList(
                "https://test-bucket.s3.fr-par.scw.cloud/test-folder/image1.jpg",
                "https://test-bucket.s3.fr-par.scw.cloud/test-folder/image2.png"
            )
        );

        // When/Then
        restS3MockMvc
            .perform(get("/api/s3/list-images").param("folderUrl", folderUrl).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.imageUrls").isArray())
            .andExpect(jsonPath("$.imageUrls", hasSize(2)))
            .andExpect(jsonPath("$.imageUrls[0]").value("https://test-bucket.s3.fr-par.scw.cloud/test-folder/image1.jpg"))
            .andExpect(jsonPath("$.imageUrls[1]").value("https://test-bucket.s3.fr-par.scw.cloud/test-folder/image2.png"));
    }

    @Test
    void shouldReturnEmptyListWhenNoImagesFound() throws Exception {
        // Given
        String folderUrl = "https://test-bucket.s3.eu-central-1.amazonaws.com/empty-folder/";
        when(s3Service.listImages(anyString())).thenReturn(Collections.emptyList());

        // When/Then
        restS3MockMvc
            .perform(get("/api/s3/list-images").param("folderUrl", folderUrl).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.imageUrls").isArray())
            .andExpect(jsonPath("$.imageUrls", hasSize(0)));
    }

    @Test
    void shouldReturnBadRequestForInvalidUrl() throws Exception {
        // Given
        String invalidUrl = "not-a-valid-url";
        when(s3Service.listImages(anyString())).thenThrow(new IllegalArgumentException("Invalid S3 folder URL"));

        // When/Then
        restS3MockMvc
            .perform(get("/api/s3/list-images").param("folderUrl", invalidUrl).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnInternalServerErrorOnS3Exception() throws Exception {
        // Given
        String folderUrl = "https://test-bucket.s3.eu-central-1.amazonaws.com/test-folder/";
        when(s3Service.listImages(anyString())).thenThrow(new RuntimeException("Failed to list images from S3"));

        // When/Then
        restS3MockMvc
            .perform(get("/api/s3/list-images").param("folderUrl", folderUrl).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }
}
