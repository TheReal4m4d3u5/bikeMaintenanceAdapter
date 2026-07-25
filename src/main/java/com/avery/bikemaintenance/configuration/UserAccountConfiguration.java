package com.avery.bikemaintenance.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.avery.bikemaintenance.adapter.outbound.memory.InMemoryUserAccountRepository;

@Configuration
public class UserAccountConfiguration {

    @Bean
    public InMemoryUserAccountRepository
            userAccountRepository() {

        return new InMemoryUserAccountRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }
}
