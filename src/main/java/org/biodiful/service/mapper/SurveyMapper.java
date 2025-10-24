package org.biodiful.service.mapper;

import org.biodiful.domain.Survey;
import org.biodiful.service.dto.SurveyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Survey} and its DTO {@link SurveyDTO}.
 */
@Mapper(componentModel = "spring")
public interface SurveyMapper extends EntityMapper<SurveyDTO, Survey> {}
