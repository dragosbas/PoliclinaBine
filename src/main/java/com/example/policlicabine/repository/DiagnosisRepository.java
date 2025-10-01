package com.example.policlicabine.repository;

import com.example.policlicabine.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, UUID> {

    Optional<Diagnosis> findByIcd10Code(String icd10Code);

    List<Diagnosis> findByIcd10CodeStartingWith(String prefix);
}
