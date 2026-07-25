package com.avery.bikemaintenance.adapter.inbound.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.avery.bikemaintenance.application.service.UserAccountService;

@Controller
public class UserAccountGraphQlController {

    private final UserAccountService userAccountService;

    public UserAccountGraphQlController(
            UserAccountService userAccountService) {

        this.userAccountService = userAccountService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @QueryMapping
    public List<TechnicianAccountResponse> technicians() {

        return userAccountService
                .findTechnicians()
                .stream()
                .map(userAccount ->
                        new TechnicianAccountResponse(
                                userAccount.getUserId(),
                                userAccount.getDisplayName(),
                                userAccount.getEmail()))
                .toList();
    }
}