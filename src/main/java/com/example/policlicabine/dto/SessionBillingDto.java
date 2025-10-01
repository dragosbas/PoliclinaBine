package com.example.policlicabine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionBillingDto {

    private UUID billingId;
    private UUID sessionId;
    private BigDecimal subtotalAmount;
    private BigDecimal totalDiscountAmount;
    private BigDecimal finalAmount;
    private List<BillingDiscountDto> discounts;
    private LocalDateTime createdAt;

    // Note: Does NOT include invoices[] to avoid circular dependency
    // Invoice → SessionBilling (DOWN hierarchy)
    // SessionBilling should NOT go back UP to Invoice
}
