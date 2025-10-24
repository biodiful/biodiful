package org.biodiful.service.impl;

import java.util.Optional;
import org.biodiful.domain.Survey;
import org.biodiful.repository.SurveyRepository;
import org.biodiful.service.SurveyService;
import org.biodiful.service.dto.SurveyDTO;
import org.biodiful.service.mapper.SurveyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.biodiful.domain.Survey}.
 */
@Service
@Transactional
public class SurveyServiceImpl implements SurveyService {

    private static final Logger LOG = LoggerFactory.getLogger(SurveyServiceImpl.class);

    private final SurveyRepository surveyRepository;

    private final SurveyMapper surveyMapper;

    public SurveyServiceImpl(SurveyRepository surveyRepository, SurveyMapper surveyMapper) {
        this.surveyRepository = surveyRepository;
        this.surveyMapper = surveyMapper;
    }

    @Override
    public SurveyDTO save(SurveyDTO surveyDTO) {
        LOG.debug("Request to save Survey : {}", surveyDTO);
        Survey survey = surveyMapper.toEntity(surveyDTO);
        survey = surveyRepository.save(survey);
        return surveyMapper.toDto(survey);
    }

    @Override
    public SurveyDTO update(SurveyDTO surveyDTO) {
        LOG.debug("Request to update Survey : {}", surveyDTO);
        Survey survey = surveyMapper.toEntity(surveyDTO);
        survey = surveyRepository.save(survey);
        return surveyMapper.toDto(survey);
    }

    @Override
    public Optional<SurveyDTO> partialUpdate(SurveyDTO surveyDTO) {
        LOG.debug("Request to partially update Survey : {}", surveyDTO);

        return surveyRepository
            .findById(surveyDTO.getId())
            .map(existingSurvey -> {
                surveyMapper.partialUpdate(existingSurvey, surveyDTO);

                return existingSurvey;
            })
            .map(surveyRepository::save)
            .map(surveyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SurveyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Surveys");
        return surveyRepository.findAll(pageable).map(surveyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SurveyDTO> findOne(Long id) {
        LOG.debug("Request to get Survey : {}", id);
        return surveyRepository.findById(id).map(surveyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Survey : {}", id);
        surveyRepository.deleteById(id);
    }
}
