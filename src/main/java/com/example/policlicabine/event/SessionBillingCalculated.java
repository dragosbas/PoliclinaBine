package com.example.policlicabine.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record SessionBillingCalculated(
    UUID billingId,
    UUID sessionId,
    UUID patientId,
    BigDecimal subtotalAmount,
    BigDecimal finalAmount,
    List<String> consultationNames
) {}
