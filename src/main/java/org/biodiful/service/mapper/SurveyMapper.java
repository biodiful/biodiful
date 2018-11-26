package org.biodiful.service.mapper;

import org.biodiful.domain.*;
import org.biodiful.service.dto.SurveyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Survey and its DTO SurveyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SurveyMapper extends EntityMapper<SurveyDTO, Survey> {



    default Survey fromId(Long id) {
        if (id == null) {
            return null;
        }
        Survey survey = new Survey();
        survey.setId(id);
        return survey;
    }
}
