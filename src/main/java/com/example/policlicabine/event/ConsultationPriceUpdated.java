package com.example.policlicabine.event;

import java.math.BigDecimal;
import java.util.UUID;

public record ConsultationPriceUpdated(
    UUID consultationId,
    String consultationName,
    BigDecimal oldPrice,
    BigDecimal newPrice
) {}
