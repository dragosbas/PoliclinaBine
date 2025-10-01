package com.example.policlicabine.event;

import java.util.UUID;

public record PatientConsentStatusChanged(
    UUID patientId,
    boolean hasConsentSigned
) {}
