package org.biodiful.service.impl;

import org.biodiful.repository.AnswerRepository;
import org.biodiful.service.SurveyService;
import org.biodiful.domain.Survey;
import org.biodiful.repository.SurveyRepository;
import org.biodiful.service.dto.SurveyDTO;
import org.biodiful.service.mapper.SurveyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Survey.
 */
@Service
@Transactional
public class SurveyServiceImpl implements SurveyService {

    private final Logger log = LoggerFactory.getLogger(SurveyServiceImpl.class);

    private final SurveyRepository surveyRepository;

    private final SurveyMapper surveyMapper;

    private final AnswerRepository answerRepository;

    public SurveyServiceImpl(SurveyRepository surveyRepository, SurveyMapper surveyMapper, AnswerRepository answerRepository) {
        this.surveyRepository = surveyRepository;
        this.surveyMapper = surveyMapper;
        this.answerRepository = answerRepository;
    }

    /**
     * Save a survey.
     *
     * @param surveyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SurveyDTO save(SurveyDTO surveyDTO) {
        log.debug("Request to save Survey : {}", surveyDTO);

        Survey survey = surveyMapper.toEntity(surveyDTO);
        survey = surveyRepository.save(survey);
        return surveyMapper.toDto(survey);
    }

    /**
     * Get all the surveys.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SurveyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Surveys");
        return surveyRepository.findAll(pageable)
            .map(surveyMapper::toDto);
    }


    public long countJudgesForSurvey(Long id) {
        return answerRepository.countDisctinctJudgeForSurvey(id);
    }


    /**
     * Get one survey by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SurveyDTO> findOne(Long id) {
        log.debug("Request to get Survey : {}", id);
        return surveyRepository.findById(id)
            .map(surveyMapper::toDto);
    }

    /**
     * Delete the survey by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Survey : {}", id);
        surveyRepository.deleteById(id);
    }
}
