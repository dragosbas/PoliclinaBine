package com.example.policlicabine.mapper;

import com.example.policlicabine.dto.WeeklyAvailabilityDto;
import com.example.policlicabine.entity.WeeklyAvailability;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WeeklyAvailabilityMapper {

    WeeklyAvailabilityDto toDto(WeeklyAvailability availability);
}
