package org.biodiful.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.biodiful.domain.ChallengerPoolTestSamples.*;

import org.biodiful.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChallengerPoolTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChallengerPool.class);
        ChallengerPool challengerPool1 = getChallengerPoolSample1();
        ChallengerPool challengerPool2 = new ChallengerPool();
        assertThat(challengerPool1).isNotEqualTo(challengerPool2);

        challengerPool2.setId(challengerPool1.getId());
        assertThat(challengerPool1).isEqualTo(challengerPool2);

        challengerPool2 = getChallengerPoolSample2();
        assertThat(challengerPool1).isNotEqualTo(challengerPool2);
    }
}
