package com.example.policlicabine.entity;

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
import java.util.stream.Collectors;

@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    private UUID paymentId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "payment_invoices",
        joinColumns = @JoinColumn(name = "payment_id"),
        inverseJoinColumns = @JoinColumn(name = "invoice_id")
    )
    private List<Invoice> invoices;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by", nullable = false)
    private User generatedBy;

    @Column(precision = 10, scale = 2, nullable = false)
    private Double amount;

    @Builder.Default
    @Column(length = 3)
    private String currency = "RON";

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void generateId() {
        if (paymentId == null) {
            paymentId = UUID.randomUUID();
        }
    }

    public List<AppointmentSession> getSessions() {
        if (invoices == null || invoices.isEmpty()) {
            return new ArrayList<>();
        }
        return invoices.stream()
            .flatMap(invoice -> invoice.getSessionBillings().stream())
            .map(SessionBilling::getSession)
            .collect(Collectors.toList());
    }

    public List<Patient> getPatients() {
        return getSessions().stream()
            .map(AppointmentSession::getPatient)
            .distinct()
            .collect(Collectors.toList());
    }

    public boolean canBeAppliedToProformaInvoice() {
        return invoices.stream().noneMatch(Invoice::getIsProforma);
    }
}
