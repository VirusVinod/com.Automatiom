
Feature: Validate Search functionality
  I want to use this template to test Search feature

  @test
  Scenario: user accept cookies
    Given user select accept cookies
     
    
    @test
  Scenario: validate search result for when enter valid input 
    Given user select accept cookies
    When user search valid product "door"

 