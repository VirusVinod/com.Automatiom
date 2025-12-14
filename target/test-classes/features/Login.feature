
Feature: Login Functionality
@Reg
  Scenario: Login With Valid Credenatials
    When user enter "standard_user" and "secret_sauce"
    And user click on login button
    Then Validate user logged in sucessfully
@Reg
  Scenario: Login With Valid Emild and Invalid password
    When user enter "standard_user" and "secret_sauce123"
    And user click on login button
    Then Validate login error massage
