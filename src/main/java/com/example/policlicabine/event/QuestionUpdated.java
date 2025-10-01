package com.example.policlicabine.event;

import java.util.UUID;

public record QuestionUpdated(
    UUID questionId,
    String oldQuestionText,
    String newQuestionText
) {}
