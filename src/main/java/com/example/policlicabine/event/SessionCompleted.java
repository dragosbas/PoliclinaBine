package com.example.policlicabine.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SessionCompleted(
    UUID sessionId,
    UUID patientId,
    UUID doctorId,
    LocalDateTime completedAt,
    List<String> consultationNames
) {}
