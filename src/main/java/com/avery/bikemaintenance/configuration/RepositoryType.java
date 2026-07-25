package com.avery.bikemaintenance.configuration;

public enum RepositoryType {
    MEMORY,
    POSTGRESQL;

    public static RepositoryType from(String value) {
        if (value == null || value.isBlank()) {
            return MEMORY;
        }

        return switch (value.trim().toLowerCase()) {
            case "memory" -> MEMORY;
            case "postgresql", "postgres" -> POSTGRESQL;
            default -> throw new IllegalArgumentException(
                    "Unknown bike repository type: " + value);
        };
    }
}