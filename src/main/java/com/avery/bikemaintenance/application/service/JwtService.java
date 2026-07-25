package com.avery.bikemaintenance.application.service;

import java.time.Duration;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.avery.bikemaintenance.domain.model.UserAccount;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final Duration expiration;
    private final String issuer;

    public JwtService(
            JwtEncoder jwtEncoder,
            @Value("${app.security.jwt-expiration-minutes:60}")
            long expirationMinutes,
            @Value("${app.security.jwt-issuer}")
            String issuer) {

        this.jwtEncoder = jwtEncoder;
        this.expiration =
                Duration.ofMinutes(expirationMinutes);
        this.issuer = issuer;
    }

    public String generateToken(
            UserAccount userAccount) {

        Instant issuedAt = Instant.now();
        Instant expiresAt =
                issuedAt.plus(expiration);

        JwtClaimsSet claims =
                JwtClaimsSet.builder()
                        .issuer(issuer)
                        .issuedAt(issuedAt)
                        .expiresAt(expiresAt)
                        .subject(userAccount.getEmail())
                        .claim(
                                "userId",
                                userAccount.getUserId())
                        .claim(
                                "role",
                                userAccount.getRole().name())
                        .claim(
                                "displayName",
                                userAccount.getDisplayName())
                        .build();

        return jwtEncoder
                .encode(
                        JwtEncoderParameters.from(
                                claims))
                .getTokenValue();
    }

    public long getExpirationSeconds() {
        return expiration.toSeconds();
    }
}