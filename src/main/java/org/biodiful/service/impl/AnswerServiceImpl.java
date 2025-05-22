package org.biodiful.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.biodiful.domain.Answer;
import org.biodiful.repository.AnswerRepository;
import org.biodiful.service.AnswerService;
import org.biodiful.service.dto.AnswerDTO;
import org.biodiful.service.mapper.AnswerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.biodiful.domain.Answer}.
 */
@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

    private static final Logger LOG = LoggerFactory.getLogger(AnswerServiceImpl.class);

    private final AnswerRepository answerRepository;

    private final AnswerMapper answerMapper;

    public AnswerServiceImpl(AnswerRepository answerRepository, AnswerMapper answerMapper) {
        this.answerRepository = answerRepository;
        this.answerMapper = answerMapper;
    }

    @Override
    public AnswerDTO save(AnswerDTO answerDTO) {
        LOG.debug("Request to save Answer : {}", answerDTO);
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
        LOG.debug("Request to save all Answers : {}", answersDTO);
        List<Answer> answers = new ArrayList<>();
        for (AnswerDTO answerDTO : answersDTO) {
            answers.add(answerMapper.toEntity(answerDTO));
        }
        answers = answerRepository.saveAll(answers);
        return answerMapper.toDto(answers);
    }

    @Override
    public AnswerDTO update(AnswerDTO answerDTO) {
        LOG.debug("Request to update Answer : {}", answerDTO);
        Answer answer = answerMapper.toEntity(answerDTO);
        answer = answerRepository.save(answer);
        return answerMapper.toDto(answer);
    }

    @Override
    public Optional<AnswerDTO> partialUpdate(AnswerDTO answerDTO) {
        LOG.debug("Request to partially update Answer : {}", answerDTO);

        return answerRepository
            .findById(answerDTO.getId())
            .map(existingAnswer -> {
                answerMapper.partialUpdate(existingAnswer, answerDTO);

                return existingAnswer;
            })
            .map(answerRepository::save)
            .map(answerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnswerDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Answers");
        return answerRepository.findAll(pageable).map(answerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnswerDTO> findOne(Long id) {
        LOG.debug("Request to get Answer : {}", id);
        return answerRepository.findById(id).map(answerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Answer : {}", id);
        answerRepository.deleteById(id);
    }

    @Override
    public List<AnswerDTO> findBySurveyId(Long surveyId) {
        return answerMapper.toDto(answerRepository.findBySurveyId(surveyId));
    }
}
