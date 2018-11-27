package org.biodiful.repository;

import org.biodiful.domain.Survey;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Survey entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

    org.springframework.data.domain.Page<Survey> findAllByOpenTrue(org.springframework.data.domain.Pageable arg0);

}