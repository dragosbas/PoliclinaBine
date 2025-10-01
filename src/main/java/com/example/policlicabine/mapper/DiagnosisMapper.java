package com.example.policlicabine.mapper;

import com.example.policlicabine.dto.DiagnosisDto;
import com.example.policlicabine.entity.Diagnosis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiagnosisMapper {

    DiagnosisDto toDto(Diagnosis diagnosis);

    @Mapping(target = "sessions", ignore = true)
    Diagnosis toEntity(DiagnosisDto dto);
}
