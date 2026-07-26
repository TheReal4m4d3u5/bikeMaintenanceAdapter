@smoke @graphql-provider
Feature: Remote GraphQL repository provider

  Scenario: Administrator sees the active remote GraphQL provider
    Given I am on the login page
    When I log in with email "admin@example.com" and password "Admin123!"
    Then I should see the administrator dashboard
    And the active repository provider should be "Repository: Remote GraphQL"
    
    
  Scenario: Administrator saves a bike through the remote GraphQL adapter
    Given I am on the login page
    When I log in with email "admin@example.com" and password "Admin123!"
    Then I should see the administrator dashboard
    When I create a uniquely named bike
    And I reload the application
    Then the created bike should appear in the fleet