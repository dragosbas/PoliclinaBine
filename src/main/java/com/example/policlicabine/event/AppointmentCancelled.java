package com.example.policlicabine.event;

import java.util.UUID;

public record AppointmentCancelled(
    UUID sessionId,
    UUID patientId,
    String reason,
    boolean wasNoShow
) {}
