package com.avery.bikemaintenance.application.port.outbound;

public record StoredCredential(
        String userId,
        String passwordHash,
        boolean enabled) {
}
