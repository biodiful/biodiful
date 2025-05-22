package org.biodiful.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.biodiful.domain.AnswerTestSamples.*;
import static org.biodiful.domain.SurveyTestSamples.*;

import org.biodiful.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnswerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Answer.class);
        Answer answer1 = getAnswerSample1();
        Answer answer2 = new Answer();
        assertThat(answer1).isNotEqualTo(answer2);

        answer2.setId(answer1.getId());
        assertThat(answer1).isEqualTo(answer2);

        answer2 = getAnswerSample2();
        assertThat(answer1).isNotEqualTo(answer2);
    }

    @Test
    void surveyTest() {
        Answer answer = getAnswerRandomSampleGenerator();
        Survey surveyBack = getSurveyRandomSampleGenerator();

        answer.setSurvey(surveyBack);
        assertThat(answer.getSurvey()).isEqualTo(surveyBack);

        answer.survey(null);
        assertThat(answer.getSurvey()).isNull();
    }
}
