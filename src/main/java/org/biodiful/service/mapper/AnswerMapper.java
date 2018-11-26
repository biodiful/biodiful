package org.biodiful.service.mapper;

import org.biodiful.domain.*;
import org.biodiful.service.dto.AnswerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Answer and its DTO AnswerDTO.
 */
@Mapper(componentModel = "spring", uses = {SurveyMapper.class})
public interface AnswerMapper extends EntityMapper<AnswerDTO, Answer> {

    @Mapping(source = "survey.id", target = "surveyId")
    AnswerDTO toDto(Answer answer);

    @Mapping(source = "surveyId", target = "survey")
    Answer toEntity(AnswerDTO answerDTO);

    default Answer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Answer answer = new Answer();
        answer.setId(id);
        return answer;
    }
}
