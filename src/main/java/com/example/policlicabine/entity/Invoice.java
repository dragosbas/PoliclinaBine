package com.example.policlicabine.entity;

import com.example.policlicabine.entity.enums.PaymentStatus;
import com.example.policlicabine.entity.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "invoices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    private UUID invoiceId;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @Column(nullable = false)
    private LocalDate invoiceDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by", nullable = false)
    private User generatedBy;

    @Builder.Default
    private Boolean isProforma = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "invoice_session_billings",
        joinColumns = @JoinColumn(name = "invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "billing_id")
    )
    private List<SessionBilling> sessionBillings;

    @ManyToMany(mappedBy = "invoices", fetch = FetchType.LAZY)
    private List<Payment> payments;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void generateId() {
        if (invoiceId == null) {
            invoiceId = UUID.randomUUID();
        }
    }

    public Double getTotalAmount() {
        if (sessionBillings == null || sessionBillings.isEmpty()) {
            return 0.0;
        }
        return sessionBillings.stream()
            .mapToDouble(SessionBilling::getFinalAmount)
            .sum();
    }

    public Double getTotalPaid() {
        if (payments == null || payments.isEmpty()) {
            return 0.0;
        }
        return payments.stream()
            .filter(payment -> payment.getPaymentType() != PaymentType.REFUND)
            .mapToDouble(Payment::getAmount)
            .sum();
    }

    public Double getOutstandingAmount() {
        return getTotalAmount() - getTotalPaid();
    }

    public PaymentStatus getPaymentStatus() {
        if (isProforma) {
            return PaymentStatus.PENDING;
        }

        Double totalPaid = getTotalPaid();
        Double totalAmount = getTotalAmount();

        if (totalPaid <= 0) {
            return PaymentStatus.PENDING;
        } else if (totalPaid >= totalAmount) {
            return PaymentStatus.FULLY_PAID;
        } else {
            return PaymentStatus.PARTIALLY_PAID;
        }
    }

    public boolean canConvertToFinalInvoice() {
        return isProforma && (payments == null || payments.isEmpty());
    }

    public void convertToFinalInvoice(String newInvoiceNumber) {
        if (!canConvertToFinalInvoice()) {
            throw new IllegalStateException("Cannot convert proforma to final invoice");
        }
        this.isProforma = false;
        this.invoiceNumber = newInvoiceNumber;
    }
}
