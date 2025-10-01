package com.example.policlicabine.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record InvoiceCreated(
    UUID invoiceId,
    String invoiceNumber,
    LocalDate invoiceDate,
    UUID generatedByUserId,
    Boolean isProforma,
    List<UUID> sessionBillingIds,
    BigDecimal totalAmount
) {}
