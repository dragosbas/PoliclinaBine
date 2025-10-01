package com.example.policlicabine.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionStarted(
    UUID sessionId,
    UUID patientId,
    UUID doctorId,
    LocalDateTime startedAt
) {}
