package com.example.policlicabine.repository;

import com.example.policlicabine.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    boolean existsByPhone(String phone);

    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByPhone(String phone);
}