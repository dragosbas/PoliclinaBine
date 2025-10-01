package com.example.policlicabine.event;

import java.util.UUID;

public record AnswerSaved(
    UUID answerId,
    UUID sessionId,
    UUID questionId,
    UUID consultationId,
    String answerText
) {}
