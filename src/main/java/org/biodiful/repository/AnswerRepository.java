package org.biodiful.repository;

import org.biodiful.domain.Answer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Answer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("select count(distinct a.judgeID) from Answer a where a.survey.id = :surveyId")
    long countDisctinctJudgeForSurvey(long surveyId);
}
