package com.example.policlicabine.event;

import java.util.UUID;

public record AnswerDeleted(
    UUID answerId,
    UUID sessionId,
    UUID questionId
) {}
