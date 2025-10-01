package com.example.policlicabine.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "diagnoses", indexes = {
    @Index(name = "idx_diagnosis_icd10", columnList = "icd10Code")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Diagnosis {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID diagnosisId;

    @Column(length = 10)
    private String icd10Code;

    @Column(columnDefinition = "TEXT")
    private String icd10Description;

    @ManyToMany(mappedBy = "diagnoses", fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private List<AppointmentSession> sessions = new ArrayList<>();

    @PrePersist
    void generateId() {
        if (diagnosisId == null) {
            diagnosisId = UUID.randomUUID();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Diagnosis)) return false;
        Diagnosis diagnosis = (Diagnosis) o;
        return diagnosisId != null && Objects.equals(diagnosisId, diagnosis.diagnosisId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Diagnosis{" +
                "diagnosisId=" + diagnosisId +
                ", icd10Code='" + icd10Code + '\'' +
                '}';
    }
}
