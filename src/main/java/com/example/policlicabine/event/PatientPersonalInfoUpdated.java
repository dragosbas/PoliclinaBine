package com.example.policlicabine.event;

import java.util.UUID;

public record PatientPersonalInfoUpdated(
    UUID patientId,
    String phone,
    String email,
    String address
) {}
