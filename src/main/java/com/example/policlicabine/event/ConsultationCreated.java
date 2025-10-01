package com.example.policlicabine.event;

import com.example.policlicabine.entity.enums.Specialty;

import java.math.BigDecimal;
import java.util.UUID;

public record ConsultationCreated(
    UUID consultationId,
    String name,
    Specialty specialty,
    BigDecimal price,
    String priceCurrency,
    Integer durationMinutes,
    Boolean requiresSurgeryRoom
) {}
