package org.biodiful.service;

import java.util.Optional;
import org.biodiful.service.dto.SurveyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.biodiful.domain.Survey}.
 */
public interface SurveyService {
    /**
     * Save a survey.
     *
     * @param surveyDTO the entity to save.
     * @return the persisted entity.
     */
    SurveyDTO save(SurveyDTO surveyDTO);

    /**
     * Updates a survey.
     *
     * @param surveyDTO the entity to update.
     * @return the persisted entity.
     */
    SurveyDTO update(SurveyDTO surveyDTO);

    /**
     * Partially updates a survey.
     *
     * @param surveyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SurveyDTO> partialUpdate(SurveyDTO surveyDTO);

    /**
     * Get all the surveys.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SurveyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" survey.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SurveyDTO> findOne(Long id);

    Optional<SurveyDTO> findOneByFriendlyURL(String friendlyURL);

    /**
     * Count the number of judges that have answered a Survey
     * @param id the id of the Survey
     * @return the number of judges that have answered a Survey
     */
    long countJudgesForSurvey(Long id);

    /**
     * Delete the "id" survey.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
