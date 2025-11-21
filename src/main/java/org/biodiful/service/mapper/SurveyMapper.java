package org.biodiful.service.mapper;

import org.biodiful.domain.Survey;
import org.biodiful.service.dto.SurveyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Survey} and its DTO {@link SurveyDTO}.
 */
@Mapper(componentModel = "spring", uses = { ChallengerPoolMapper.class })
public interface SurveyMapper extends EntityMapper<SurveyDTO, Survey> {
    @Mapping(target = "removeChallengerPool", ignore = true)
    Survey toEntity(SurveyDTO dto);

    @Mapping(target = "removeChallengerPool", ignore = true)
    void partialUpdate(@MappingTarget Survey entity, SurveyDTO dto);
}
