package com.example.policlicabine.event;

import com.example.policlicabine.entity.enums.UserRole;

import java.util.UUID;

public record UserCreated(
    UUID userId,
    String username,
    String fullName,
    UserRole role
) {}
