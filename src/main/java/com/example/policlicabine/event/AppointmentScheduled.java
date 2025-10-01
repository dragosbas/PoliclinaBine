package com.example.policlicabine.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AppointmentScheduled(
    UUID sessionId,
    UUID patientId,
    UUID doctorId,
    LocalDateTime scheduledDateTime,
    List<String> consultationNames,
    boolean isEmergency
) {}
