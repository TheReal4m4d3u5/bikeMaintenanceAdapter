package com.avery.bikemaintenance.configuration;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfiguration {

    @Bean
    public SecretKey jwtSecretKey(
            @Value("${app.security.jwt-secret}")
            String encodedSecret) {

        byte[] secretBytes =
                Base64.getDecoder().decode(encodedSecret);

        if (secretBytes.length < 32) {
            throw new IllegalStateException(
                    "JWT secret must contain at least 32 bytes");
        }

        return new SecretKeySpec(
                secretBytes,
                "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder(
            SecretKey jwtSecretKey) {

        return NimbusJwtEncoder
                .withSecretKey(jwtSecretKey)
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(
            SecretKey jwtSecretKey,
            @Value("${app.security.jwt-issuer}")
            String issuer) {

        NimbusJwtDecoder jwtDecoder =
                NimbusJwtDecoder
                        .withSecretKey(jwtSecretKey)
                        .build();

        jwtDecoder.setJwtValidator(
                JwtValidators.createDefaultWithIssuer(
                        issuer));

        return jwtDecoder;
    }
}