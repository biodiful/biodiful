package org.biodiful.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.biodiful.domain.SurveyAsserts.*;
import static org.biodiful.domain.SurveyTestSamples.*;

import org.biodiful.domain.ChallengerPool;
import org.biodiful.domain.Survey;
import org.biodiful.service.dto.SurveyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SurveyMapperTest {

    private SurveyMapper surveyMapper;

    @BeforeEach
    void setUp() {
        SurveyMapperImpl surveyMapperImpl = new SurveyMapperImpl();
        // Manually inject the ChallengerPoolMapper dependency
        try {
            java.lang.reflect.Field field = SurveyMapperImpl.class.getDeclaredField("challengerPoolMapper");
            field.setAccessible(true);
            field.set(surveyMapperImpl, new ChallengerPoolMapperImpl());
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject ChallengerPoolMapper", e);
        }
        surveyMapper = surveyMapperImpl;
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSurveySample1();
        var actual = surveyMapper.toEntity(surveyMapper.toDto(expected));
        assertSurveyAllPropertiesEquals(expected, actual);
    }

    @Test
    void shouldMapSurveyWithMultiplePools() {
        // Given a survey with multiple pools
        Survey survey = getSurveyWithMultiplePools();

        // When converting to DTO
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        // Then the pools should be mapped
        assertThat(surveyDTO.getChallengerPools()).hasSize(5);
        assertThat(surveyDTO.getChallengerPools()).extracting("poolOrder").containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    void shouldMapSurveyWithSinglePool() {
        // Given a survey with one pool
        Survey survey = getSurveySample1();

        // When converting to DTO
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        // Then the pool should be mapped
        assertThat(surveyDTO.getChallengerPools()).hasSize(1);
        assertThat(surveyDTO.getChallengerPools().iterator().next().getPoolOrder()).isEqualTo(1);
    }

    @Test
    void shouldPreservePoolOrderingWhenMapping() {
        // Given a survey with multiple pools
        Survey survey = getSurveySample2();

        // When converting to DTO and back
        SurveyDTO dto = surveyMapper.toDto(survey);
        Survey mappedSurvey = surveyMapper.toEntity(dto);

        // Then the pool ordering should be preserved
        assertThat(mappedSurvey.getChallengerPools()).hasSize(2);
        assertThat(mappedSurvey.getChallengerPools()).extracting(ChallengerPool::getPoolOrder).containsExactlyInAnyOrder(1, 2);
    }
}
