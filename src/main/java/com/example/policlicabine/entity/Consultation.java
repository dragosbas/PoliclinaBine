package com.example.policlicabine.entity;

import com.example.policlicabine.entity.enums.Specialty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "consultations", indexes = {
    @Index(name = "idx_consultation_specialty", columnList = "specialty"),
    @Index(name = "idx_consultation_active", columnList = "isActive")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Consultation {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID consultationId;

    @Column(nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    private Specialty specialty;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 3)
    private String priceCurrency = "RON";

    private Integer durationMinutes;

    private Boolean requiresSurgeryRoom = false;

    private Boolean isActive = true;

    @OneToMany(mappedBy = "consultation", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void generateId() {
        if (consultationId == null) {
            consultationId = UUID.randomUUID();
        }
    }

    public Boolean getRequiresSurgeryRoom() {
        return requiresSurgeryRoom != null && requiresSurgeryRoom;
    }

    // Helper method for bidirectional relationship
    public void addQuestion(Question question) {
        questions.add(question);
        question.setConsultation(this);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setConsultation(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Consultation)) return false;
        Consultation that = (Consultation) o;
        return consultationId != null && Objects.equals(consultationId, that.consultationId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "consultationId=" + consultationId +
                ", name='" + name + '\'' +
                ", specialty=" + specialty +
                ", price=" + price +
                '}';
    }
}
