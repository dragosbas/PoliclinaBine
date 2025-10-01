package com.example.policlicabine.event;

import java.util.UUID;

public record AnswerUpdated(
    UUID answerId,
    String oldAnswerText,
    String newAnswerText
) {}
