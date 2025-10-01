package com.example.policlicabine.repository;

import com.example.policlicabine.entity.AppointmentSession;
import com.example.policlicabine.entity.enums.SessionStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentSessionRepository extends JpaRepository<AppointmentSession, UUID> {

    // ============= EntityGraph Query Methods =============
    // These methods use @EntityGraph to prevent N+1 query problems
    // by eagerly fetching specified relationships in a single query

    /**
     * Finds appointment session with patient and doctor loaded.
     * Use for operations that need basic relationship data.
     */
    @EntityGraph(attributePaths = {"patient", "doctor"})
    Optional<AppointmentSession> findWithBasicRelationshipsById(UUID sessionId);

    /**
     * Finds appointment session with all relationships loaded.
     * Use for DTO mapping where all data is needed.
     * Includes answers for complete nested DTO mapping.
     */
    @EntityGraph(attributePaths = {"patient", "doctor", "consultations", "diagnoses", "answers"})
    Optional<AppointmentSession> findWithAllRelationshipsById(UUID sessionId);

    /**
     * Finds appointment session with consultations loaded.
     * Use when adding consultations to a session.
     */
    @EntityGraph(attributePaths = {"consultations"})
    Optional<AppointmentSession> findWithConsultationsById(UUID sessionId);

    /**
     * Finds patient's appointment history with relationships loaded.
     * Prevents N+1 queries when mapping to DTOs.
     */
    @EntityGraph(attributePaths = {"patient", "doctor", "consultations"})
    List<AppointmentSession> findWithRelationshipsByPatientPatientIdOrderByScheduledDateTimeDesc(UUID patientId);

    /**
     * Finds doctor's appointments in date range with patient loaded.
     * Used for medical file access control.
     */
    @EntityGraph(attributePaths = {"patient", "doctor"})
    List<AppointmentSession> findWithBasicRelationshipsByDoctorDoctorIdAndScheduledDateTimeBetweenAndStatusNot(
            UUID doctorId, LocalDateTime start, LocalDateTime end, SessionStatus excludeStatus);

    // ============= Standard Query Methods =============

    List<AppointmentSession> findByPatientPatientIdOrderByScheduledDateTimeDesc(UUID patientId);

    List<AppointmentSession> findByDoctorDoctorIdAndScheduledDateTimeBetween(
            UUID doctorId, LocalDateTime start, LocalDateTime end);

    List<AppointmentSession> findByDoctorDoctorIdAndScheduledDateTimeBetweenAndStatusNot(
            UUID doctorId, LocalDateTime start, LocalDateTime end, SessionStatus status);

    boolean existsByDoctorDoctorIdAndPatientPatientIdAndScheduledDateTimeBetweenAndStatusNot(
            UUID doctorId, UUID patientId, LocalDateTime start, LocalDateTime end, SessionStatus status);

    @Query("SELECT a FROM AppointmentSession a WHERE a.doctor.doctorId = :doctorId " +
           "AND a.scheduledDateTime BETWEEN :start AND :end " +
           "AND a.status = :status")
    List<AppointmentSession> findDoctorAppointmentsByDateAndStatus(
            @Param("doctorId") UUID doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") SessionStatus status);

    List<AppointmentSession> findByStatus(SessionStatus status);
}
