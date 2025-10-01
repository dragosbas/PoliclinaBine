package com.example.policlicabine.event;

import java.util.UUID;

public record DiagnosisCreated(
    UUID diagnosisId,
    String icd10Code,
    String icd10Description
) {}
