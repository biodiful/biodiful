package org.biodiful.service.mapper;

import org.biodiful.domain.Answer;
import org.biodiful.domain.Survey;
import org.biodiful.service.dto.AnswerDTO;
import org.biodiful.service.dto.SurveyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Answer} and its DTO {@link AnswerDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnswerMapper extends EntityMapper<AnswerDTO, Answer> {
    @Mapping(target = "survey", source = "survey", qualifiedByName = "surveyId")
    AnswerDTO toDto(Answer s);

    @Named("surveyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SurveyDTO toDtoSurveyId(Survey survey);
}
