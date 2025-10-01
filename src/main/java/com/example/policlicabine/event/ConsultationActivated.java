package com.example.policlicabine.event;

import java.util.UUID;

public record ConsultationActivated(
    UUID consultationId,
    String consultationName
) {}
