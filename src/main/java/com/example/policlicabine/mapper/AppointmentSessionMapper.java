package com.example.policlicabine.mapper;

import com.example.policlicabine.dto.AppointmentSessionDto;
import com.example.policlicabine.entity.AppointmentSession;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
    PatientMapper.class,
    DoctorMapper.class,
    ConsultationMapper.class,
    DiagnosisMapper.class,
    AnswerMapper.class
})
public interface AppointmentSessionMapper {

    /**
     * Maps AppointmentSession entity to DTO with nested DTOs (going DOWN the hierarchy).
     *
     * MapStruct automatically maps:
     * - patient → PatientDto (via PatientMapper)
     * - doctor → DoctorDto (via DoctorMapper)
     * - consultations → List<ConsultationDto> (via ConsultationMapper)
     * - diagnoses → List<DiagnosisDto> (via DiagnosisMapper)
     * - answers → List<AnswerDto> (via AnswerMapper)
     */
    AppointmentSessionDto toDto(AppointmentSession session);
}
