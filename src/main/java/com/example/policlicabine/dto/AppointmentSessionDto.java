package com.example.policlicabine.dto;

import com.example.policlicabine.entity.enums.SessionStatus;
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
public class AppointmentSessionDto {

    private UUID sessionId;

    // Nested DTOs (going DOWN the hierarchy)
    private PatientDto patient;
    private DoctorDto doctor;
    private List<ConsultationDto> consultations;
    private List<DiagnosisDto> diagnoses;
    private List<AnswerDto> answers;

    // Session details
    private LocalDateTime scheduledDateTime;
    private Boolean isEmergency;
    private SessionStatus status;
    private String freeTextDiagnosis;
    private String treatmentInstructions;
    private String freeTextObservations;
    private String cancellationReason;
    private Integer contactAttempts;
    private Integer rescheduleCount;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private BigDecimal subtotalAmount;
}
