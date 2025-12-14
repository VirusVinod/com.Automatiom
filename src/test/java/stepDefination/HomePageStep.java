package stepDefination;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import page.HomePage;

public class HomePageStep {

	HomePage homepage = new HomePage();

	@Given("user select accept cookies")
	public void user_select_accept_cookies() {
		homepage.clickOnAcceptCookies();

	}

	@When("user search valid product {string}")
	public void user_search_valid_product(String productName) {
		homepage.searchProduct(productName);
	}
}
