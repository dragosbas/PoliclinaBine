package com.example.policlicabine.entity;

import com.example.policlicabine.entity.enums.PaymentStatus;
import com.example.policlicabine.entity.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "session_billing")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionBilling {

    @Id
    private UUID billingId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false, unique = true)
    private AppointmentSession session;

    @OneToMany(mappedBy = "sessionBilling", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BillingDiscount> discounts;

    @ManyToMany(mappedBy = "sessionBillings", fetch = FetchType.LAZY)
    private List<Invoice> invoices;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void generateId() {
        if (billingId == null) {
            billingId = UUID.randomUUID();
        }
    }

    public Double getSubtotalAmount() {
        return session != null ? session.getSubtotalAmount() : 0.0;
    }

    public Double getTotalDiscountAmount() {
        if (discounts == null || discounts.isEmpty()) {
            return 0.0;
        }
        return discounts.stream()
            .mapToDouble(discount -> discount.getAmount() != null ? discount.getAmount() : 0.0)
            .sum();
    }

    public Double getFinalAmount() {
        return getSubtotalAmount() - getTotalDiscountAmount();
    }

    public PaymentStatus getPaymentStatus() {
        if (invoices == null || invoices.isEmpty()) {
            return PaymentStatus.PENDING;
        }

        Double totalPaid = invoices.stream()
            .flatMap(invoice -> invoice.getPayments().stream())
            .flatMap(payment -> payment.getInvoices().stream())
            .filter(invoice -> invoice.getSessionBillings().contains(this))
            .flatMap(invoice -> invoice.getPayments().stream())
            .filter(payment -> payment.getPaymentType() != PaymentType.REFUND)
            .mapToDouble(Payment::getAmount)
            .sum();

        Double finalAmount = getFinalAmount();

        if (totalPaid <= 0) {
            return PaymentStatus.PENDING;
        } else if (totalPaid >= finalAmount) {
            return PaymentStatus.FULLY_PAID;
        } else {
            return PaymentStatus.PARTIALLY_PAID;
        }
    }

    public Patient getPatient() {
        return session != null ? session.getPatient() : null;
    }

    public void addDiscount(User appliedBy, Double amount, String reason) {
        if (discounts == null) {
            discounts = new ArrayList<>();
        }

        BillingDiscount discount = BillingDiscount.builder()
            .sessionBilling(this)
            .appliedBy(appliedBy)
            .amount(amount)
            .reason(reason)
            .build();

        discounts.add(discount);
    }
}
