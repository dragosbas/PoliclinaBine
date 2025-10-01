package com.example.policlicabine.mapper;

import com.example.policlicabine.dto.AnswerDto;
import com.example.policlicabine.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    @Mapping(target = "sessionId", source = "session.sessionId")
    @Mapping(target = "questionId", source = "question.questionId")
    @Mapping(target = "questionText", source = "question.questionText")
    @Mapping(target = "consultationId", source = "consultation.consultationId")
    @Mapping(target = "consultationName", source = "consultation.name")
    AnswerDto toDto(Answer answer);
}
