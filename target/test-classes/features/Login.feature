
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

@Reg    
    Scenario: Login With Invalid Emild and Valid password
    When user enter "Vinod" and "secret_sauce"
    And user click on login button
    Then Validate login error massage
@Reg
     Scenario: Login With Blank Emild and  password
    When user enter "  and " "
    And user click on login button
    Then Verify the login error message for blank email and password fields.
    
@Reg
  Scenario: Product add to cart
    When user enter "standard_user" and "secret_sauce"
    And user click on login button
    And add a product add to cart
    Then Validate shopping cart badge