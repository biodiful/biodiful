package org.biodiful.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.biodiful.domain.SurveyTestSamples.*;

import org.biodiful.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SurveyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Survey.class);
        Survey survey1 = getSurveySample1();
        Survey survey2 = new Survey();
        assertThat(survey1).isNotEqualTo(survey2);

        survey2.setId(survey1.getId());
        assertThat(survey1).isEqualTo(survey2);

        survey2 = getSurveySample2();
        assertThat(survey1).isNotEqualTo(survey2);
    }
}
