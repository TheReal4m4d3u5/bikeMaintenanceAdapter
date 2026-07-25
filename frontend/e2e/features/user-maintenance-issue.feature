@smoke @user @maintenance-issue
Feature: User reports a maintenance issue

  As a bike-share user
  I want to report a problem with an available bike
  So that the maintenance team can investigate it

  Background:
    Given I am on the login page
    When I log in with email "user@example.com" and password "User12345!"

  Scenario: User submits a maintenance issue
    Then I should see the user dashboard
    And I should see at least one available bike
    When I report a medium user complaint for the first available bike
    Then the submitted maintenance issue should appear in my reported issues