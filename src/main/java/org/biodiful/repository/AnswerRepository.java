package org.biodiful.repository;

import org.biodiful.domain.Answer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Answer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {}
