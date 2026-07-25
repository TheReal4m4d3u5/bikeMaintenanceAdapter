package com.avery.bikemaintenance.adapter.inbound.graphql;

public record TechnicianAccountResponse(
        String userId,
        String displayName,
        String email) {
}