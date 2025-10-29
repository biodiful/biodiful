package org.biodiful.service.mapper;

import static org.biodiful.domain.ChallengerPoolAsserts.*;
import static org.biodiful.domain.ChallengerPoolTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChallengerPoolMapperTest {

    private ChallengerPoolMapper challengerPoolMapper;

    @BeforeEach
    void setUp() {
        challengerPoolMapper = new ChallengerPoolMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChallengerPoolSample1();
        var actual = challengerPoolMapper.toEntity(challengerPoolMapper.toDto(expected));
        assertChallengerPoolAllPropertiesEquals(expected, actual);
    }
}
