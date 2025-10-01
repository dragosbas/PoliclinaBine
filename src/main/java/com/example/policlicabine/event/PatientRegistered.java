package com.example.policlicabine.event;

import java.util.UUID;

public record PatientRegistered(
    UUID patientId,
    String firstName,
    String lastName,
    String phone,
    String email
) {}
