package com.example.policlicabine.dto;

import com.example.policlicabine.entity.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

    private UUID invoiceId;
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private UUID generatedByUserId;
    private String generatedByUsername;
    private Boolean isProforma;
    private List<UUID> sessionBillingIds;
    private BigDecimal totalAmount;
    private BigDecimal totalPaid;
    private BigDecimal outstandingAmount;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
}