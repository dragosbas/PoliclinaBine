package com.example.policlicabine.event;

import java.util.UUID;

public record QuestionCreated(
    UUID questionId,
    UUID consultationId,
    String consultationName,
    String questionText
) {}
