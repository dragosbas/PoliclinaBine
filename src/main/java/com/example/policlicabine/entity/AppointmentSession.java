package com.example.policlicabine.entity;

import com.example.policlicabine.entity.enums.SessionStatus;
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
@Table(name = "appointment_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSession {

    @Id
    private UUID sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDateTime scheduledDateTime;

    @Builder.Default
    private Boolean isEmergency = false;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SessionStatus status = SessionStatus.SCHEDULED;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "session_consultations",
        joinColumns = @JoinColumn(name = "session_id"),
        inverseJoinColumns = @JoinColumn(name = "consultation_id")
    )
    private List<Consultation> consultations;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "session_diagnoses",
        joinColumns = @JoinColumn(name = "session_id"),
        inverseJoinColumns = @JoinColumn(name = "diagnosis_id")
    )
    private List<Diagnosis> diagnoses;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answers;

    @Column(columnDefinition = "TEXT")
    private String freeTextDiagnosis;

    @Column(columnDefinition = "TEXT")
    private String treatmentInstructions;

    @Column(columnDefinition = "TEXT")
    private String freeTextObservations;

    @Column(columnDefinition = "TEXT")
    private String cancellationReason;

    @Builder.Default
    private Integer contactAttempts = 0;

    @Builder.Default
    private Integer rescheduleCount = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private LocalDateTime lastContactAttemptAt;

    @PrePersist
    void generateId() {
        if (sessionId == null) {
            sessionId = UUID.randomUUID();
        }
    }

    public boolean requiresSurgeryRoom() {
        return consultations != null && consultations.stream()
            .anyMatch(Consultation::getRequiresSurgeryRoom);
    }

    public boolean isCompleted() {
        return status == SessionStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return status == SessionStatus.CANCELLED || status == SessionStatus.NO_SHOW;
    }

    public boolean hasBeenRescheduled() {
        return rescheduleCount > 0;
    }

    public boolean hasMedicalData() {
        return (diagnoses != null && !diagnoses.isEmpty()) ||
               freeTextDiagnosis != null ||
               treatmentInstructions != null ||
               freeTextObservations != null;
    }

    public Double getSubtotalAmount() {
        if (consultations == null || consultations.isEmpty()) {
            return 0.0;
        }
        return consultations.stream()
            .mapToDouble(consultation -> consultation.getPrice() != null ? consultation.getPrice() : 0.0)
            .sum();
    }

    public List<Question> getAllQuestions() {
        if (consultations == null || consultations.isEmpty()) {
            return new ArrayList<>();
        }
        return consultations.stream()
            .flatMap(consultation -> consultation.getQuestions().stream())
            .collect(Collectors.toList());
    }

    public List<Answer> getAnswersForConsultation(Consultation consultation) {
        if (answers == null || answers.isEmpty()) {
            return new ArrayList<>();
        }
        return answers.stream()
            .filter(answer -> consultation.equals(answer.getConsultation()))
            .collect(Collectors.toList());
    }
}
