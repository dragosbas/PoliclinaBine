package com.example.policlicabine.event;

import java.math.BigDecimal;
import java.util.UUID;

public record ConsultationTypeAdded(
    UUID sessionId,
    String consultationName,
    boolean isActive,
    BigDecimal price
) {}
