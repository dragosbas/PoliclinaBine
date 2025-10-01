package com.example.policlicabine.dto;

import com.example.policlicabine.entity.enums.Specialty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationDto {

    private UUID consultationId;
    private String name;
    private Specialty specialty;
    private BigDecimal price;
    private String priceCurrency;
    private Integer durationMinutes;
    private Boolean requiresSurgeryRoom;
    private Boolean isActive;
    private List<QuestionDto> questions;
}
