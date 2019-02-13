package org.biodiful.service.impl;

import org.biodiful.service.AnswerService;
import org.biodiful.domain.Answer;
import org.biodiful.repository.AnswerRepository;
import org.biodiful.service.dto.AnswerDTO;
import org.biodiful.service.mapper.AnswerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Answer.
 */
@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

    private final Logger log = LoggerFactory.getLogger(AnswerServiceImpl.class);

    private final AnswerRepository answerRepository;

    private final AnswerMapper answerMapper;

    public AnswerServiceImpl(AnswerRepository answerRepository, AnswerMapper answerMapper) {
        this.answerRepository = answerRepository;
        this.answerMapper = answerMapper;
    }

    /**
     * Save a answer.
     *
     * @param answerDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AnswerDTO save(AnswerDTO answerDTO) {
        log.debug("Request to save Answer : {}", answerDTO);

        Answer answer = answerMapper.toEntity(answerDTO);
        answer = answerRepository.save(answer);
        return answerMapper.toDto(answer);
    }


    /**
     * Save a list of answer.
     *
     * @param answersDTO the entities to save
     * @return the persisted entities
     */
    @Override
    public List<AnswerDTO> saveAll(List<AnswerDTO> answersDTO) {
        log.debug("Request to save all Answers : {}", answersDTO);

        List<Answer> answers = new ArrayList<Answer>();
        for (AnswerDTO answerDTO : answersDTO) {
            answers.add(answerMapper.toEntity(answerDTO));
        }
        answers = answerRepository.saveAll(answers);
        return answerMapper.toDto(answers);
    }

    /**
     * Get all the answers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AnswerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Answers");
        return answerRepository.findAll(pageable)
            .map(answerMapper::toDto);
    }


    /**
     * Get one answer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AnswerDTO> findOne(Long id) {
        log.debug("Request to get Answer : {}", id);
        return answerRepository.findById(id)
            .map(answerMapper::toDto);
    }

    /**
     * Delete the answer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Answer : {}", id);
        answerRepository.deleteById(id);
    }

    @Override
    public List<AnswerDTO> findBySurveyId(Long surveyId) {
        return answerMapper.toDto(answerRepository.findBySurveyId(surveyId));
    }
}
