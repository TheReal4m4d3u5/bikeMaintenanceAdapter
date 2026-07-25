package com.avery.bikemaintenance.configuration;

import org.springframework.context.annotation.Profile;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.avery.bikemaintenance.application.port.outbound.UserAccountRepository;
import com.avery.bikemaintenance.domain.model.UserAccount;
import com.avery.bikemaintenance.domain.model.UserRole;

@Component
@Profile("!prod")
public class DevelopmentUserAccountInitializer
        implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed-data:false}")
    private boolean seedData;

    public DevelopmentUserAccountInitializer(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder) {

        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (!seedData) {
            return;
        }

        seedAccount(
                "ADMIN-1001",
                "admin@example.com",
                "System Administrator",
                "Admin123!",
                UserRole.ADMIN);

        seedAccount(
                "TECH-1001",
                "technician@example.com",
                "Jordan Lee",
                "Tech12345!",
                UserRole.TECHNICIAN);
        
        seedAccount(
                "USER-1001",
                "user@example.com",
                "Test User",
                "User12345!",
                UserRole.USER);

        System.out.println(
                "Development user accounts initialized.");
    }

    private void seedAccount(
            String userId,
            String email,
            String displayName,
            String rawPassword,
            UserRole role) {

        if (userAccountRepository.existsByEmail(email)) {
            return;
        }

        UserAccount account =
                new UserAccount(
                        userId,
                        email,
                        displayName,
                        passwordEncoder.encode(rawPassword),
                        role);

        userAccountRepository.save(account);
    }
}
