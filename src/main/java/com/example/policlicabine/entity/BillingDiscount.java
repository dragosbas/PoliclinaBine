package com.example.policlicabine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "billing_discounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingDiscount {

    @Id
    private UUID discountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_billing_id", nullable = false)
    private SessionBilling sessionBilling;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applied_by", nullable = false)
    private User appliedBy;

    @Column(precision = 10, scale = 2, nullable = false)
    private Double amount;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void generateId() {
        if (discountId == null) {
            discountId = UUID.randomUUID();
        }
    }
}
