package com.example.policlicabine.event;

import java.util.UUID;

public record ConsultationDeactivated(
    UUID consultationId,
    String consultationName
) {}
