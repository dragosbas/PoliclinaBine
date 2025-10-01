package com.example.policlicabine.event;

import java.math.BigDecimal;
import java.util.UUID;

public record ManualDiscountApplied(
    UUID billingId,
    UUID sessionId,
    BigDecimal discountAmount,
    String reason,
    UUID appliedByUserId
) {}
