package com.example.policlicabine.event;

import java.util.UUID;

public record QuestionDeleted(
    UUID questionId,
    UUID consultationId,
    String questionText
) {}
