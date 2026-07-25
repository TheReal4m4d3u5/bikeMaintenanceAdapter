@smoke @memory-provider
Feature: In-memory maintenance repository provider

  As a system administrator
  I want to know which repository provider is active
  So that I can verify the application is using the expected adapter family

  Background:
    Given I am on the login page
    When I log in with email "admin@example.com" and password "Admin123!"

  Scenario: Administrator sees the active in-memory provider
    Then I should see the administrator dashboard
    And the active repository provider should be "Repository: In Memory"

  Scenario: Seeded fleet data is available
    Then I should see the administrator dashboard
    And I should see at least one fleet bike