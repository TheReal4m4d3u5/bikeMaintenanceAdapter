package com.avery.bikemaintenance.adapter.inbound.rest;

public record LoginRequest(
        String email,
        String password) {
}