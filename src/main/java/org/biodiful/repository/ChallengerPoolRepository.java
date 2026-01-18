package org.biodiful.repository;

import java.util.List;
import org.biodiful.domain.ChallengerPool;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChallengerPool entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChallengerPoolRepository extends JpaRepository<ChallengerPool, Long> {
    @Query("select cp from ChallengerPool cp where cp.survey.id = :surveyId order by cp.poolOrder")
    List<ChallengerPool> findBySurveyIdOrderByPoolOrder(long surveyId);

    void deleteBySurveyId(Long surveyId);
}
