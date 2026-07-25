package com.avery.bikemaintenance.domain.model;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

public class UserAccount {

    private final String userId;
    private final String email;
    private String displayName;
    private final String passwordHash;
    private final UserRole role;
    private boolean enabled;
    private final LocalDateTime createdDate;

    public UserAccount(
            String userId,
            String email,
            String displayName,
            String passwordHash,
            UserRole role) {

        this.userId = requireText(userId, "User ID");
        this.email = normalizeEmail(email);
        this.displayName = requireText(displayName, "Display name");
        this.passwordHash = requireText(passwordHash, "Password hash");
        this.role = Objects.requireNonNull(role, "Role is required");
        this.enabled = true;
        this.createdDate = LocalDateTime.now();
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void updateDisplayName(String displayName) {
        this.displayName = requireText(displayName, "Display name");
    }

    public void disable() {
        enabled = false;
    }

    public void enable() {
        enabled = true;
    }

    private static String normalizeEmail(String email) {
        return requireText(email, "Email")
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " is required");
        }

        return value.trim();
    }
}