package com.example.policlicabine.event;

import java.util.List;
import java.util.UUID;

public record SessionDocumentationCompleted(
    UUID sessionId,
    UUID patientId,
    UUID doctorId,
    String freeTextDiagnosis,
    String treatmentInstructions,
    List<String> consultationNames
) {}
