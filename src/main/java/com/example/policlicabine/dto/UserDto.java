package com.example.policlicabine.dto;

import com.example.policlicabine.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID userId;
    private String username;
    private String fullName;
    private UserRole role;
}
