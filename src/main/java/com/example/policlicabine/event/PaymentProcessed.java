package com.example.policlicabine.event;

import com.example.policlicabine.entity.enums.PaymentType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PaymentProcessed(
    UUID paymentId,
    List<UUID> invoiceIds,
    BigDecimal amount,
    PaymentType paymentType,
    List<UUID> patientIds
) {}
