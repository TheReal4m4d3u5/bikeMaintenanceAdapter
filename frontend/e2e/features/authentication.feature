@smoke @authentication
Feature: Administrator authentication

  As a system administrator
  I want to log in to the application
  So that I can manage the bike-maintenance system

  Scenario: Administrator logs in with valid credentials
    Given I am on the login page
    When I log in with email "admin@example.com" and password "Admin123!"
    Then I should see the administrator dashboard
    And I should be signed in as "System Administrator"