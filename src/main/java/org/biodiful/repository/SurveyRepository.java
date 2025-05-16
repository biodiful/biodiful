package org.biodiful.repository;

import java.util.Optional;
import org.biodiful.domain.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Survey entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Page<Survey> findAllByOpenTrue(Pageable pageable);

    Optional<Survey> findByFriendlyURL(String friendlyURL);
}
