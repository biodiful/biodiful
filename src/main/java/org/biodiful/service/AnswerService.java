package org.biodiful.service;

import java.util.List;
import java.util.Optional;
import org.biodiful.service.dto.AnswerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.biodiful.domain.Answer}.
 */
public interface AnswerService {
    /**
     * Save a answer.
     *
     * @param answerDTO the entity to save.
     * @return the persisted entity.
     */
    AnswerDTO save(AnswerDTO answerDTO);

    List<AnswerDTO> saveAll(List<AnswerDTO> answersDTO);

    /**
     * Updates a answer.
     *
     * @param answerDTO the entity to update.
     * @return the persisted entity.
     */
    AnswerDTO update(AnswerDTO answerDTO);

    /**
     * Partially updates a answer.
     *
     * @param answerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AnswerDTO> partialUpdate(AnswerDTO answerDTO);

    /**
     * Get all the answers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnswerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" answer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnswerDTO> findOne(Long id);

    /**
     * Delete the "id" answer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the answers for the provided survey ID.
     *
     * @param surveyId the id of the survey for which the answers will be returned
     * @return the list of entities
     */
    List<AnswerDTO> findBySurveyId(Long surveyId);
}
