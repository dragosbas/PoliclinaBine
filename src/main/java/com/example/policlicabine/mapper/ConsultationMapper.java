package com.example.policlicabine.mapper;

import com.example.policlicabine.dto.ConsultationDto;
import com.example.policlicabine.entity.Consultation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface ConsultationMapper {

    ConsultationDto toDto(Consultation consultation);
}
