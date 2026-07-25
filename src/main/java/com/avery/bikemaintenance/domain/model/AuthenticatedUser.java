package com.avery.bikemaintenance.domain.model;

import java.util.Objects;

public record AuthenticatedUser(
        String userId,
        UserRole role) {

    public AuthenticatedUser {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException(
                    "Authenticated user ID is required.");
        }

        Objects.requireNonNull(
                role,
                "Authenticated user role is required.");
    }
}
