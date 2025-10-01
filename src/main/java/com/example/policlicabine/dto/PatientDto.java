package com.example.policlicabine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {

    private UUID patientId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private String consentFileUrl;
    private LocalDateTime registrationDate;
}
