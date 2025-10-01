package com.example.policlicabine.mapper;

import com.example.policlicabine.dto.QuestionDto;
import com.example.policlicabine.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "consultationId", source = "consultation.consultationId")
    @Mapping(target = "consultationName", source = "consultation.name")
    QuestionDto toDto(Question question);
}
