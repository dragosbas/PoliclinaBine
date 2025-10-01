package com.example.policlicabine.event;

import com.example.policlicabine.entity.enums.Specialty;

import java.util.List;
import java.util.UUID;

/**
 * Domain event published when a new doctor profile is created.
 *
 * This event enables decoupled architecture by allowing other services
 * to react to doctor profile creation without direct dependencies.
 *
 * Use cases:
 * - Notification service can send welcome email to doctor
 * - Analytics service can track new doctor registrations
 * - Scheduling service can initialize doctor availability
 */
public record DoctorProfileCreated(
    UUID doctorId,
    UUID userId,
    String userName,
    List<Specialty> specialties
) {}
