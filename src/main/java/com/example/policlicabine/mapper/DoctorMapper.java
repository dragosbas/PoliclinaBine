package com.example.policlicabine.mapper;

import com.example.policlicabine.dto.DoctorDto;
import com.example.policlicabine.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {WeeklyAvailabilityMapper.class})
public interface DoctorMapper {

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "fullName", source = "user.fullName")
    DoctorDto toDto(Doctor doctor);
}
