package org.biodiful.service.mapper;

import org.biodiful.domain.ChallengerPool;
import org.biodiful.domain.Survey;
import org.biodiful.service.dto.ChallengerPoolDTO;
import org.biodiful.service.dto.SurveyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChallengerPool} and its DTO {@link ChallengerPoolDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChallengerPoolMapper extends EntityMapper<ChallengerPoolDTO, ChallengerPool> {
    @Mapping(target = "surveyId", source = "survey.id")
    ChallengerPoolDTO toDto(ChallengerPool challengerPool);

    @Mapping(target = "survey", ignore = true)
    ChallengerPool toEntity(ChallengerPoolDTO challengerPoolDTO);

    @Mapping(target = "survey", ignore = true)
    void partialUpdate(@MappingTarget ChallengerPool entity, ChallengerPoolDTO dto);
}
