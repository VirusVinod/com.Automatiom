package stepDefination;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import page.LoginPage;

public class LoginPageSteps {

	LoginPage login = new LoginPage();

	@When("user enter {string} and {string}")
	public void user_enter_and(String uname, String pass) {
		login.user_enter_and(uname, pass);
	}

	@When("user click on login button")
	public void user_click_on_login_button() {
		login.user_click_on_login_button();
	}

	@When("Validate user logged in sucessfully")
	public void validate_user_logged_in_sucessfully() {
		login.validate_user_logged_in_sucessfully();
	}

	@Then("Validate login error massage")
	public void validate_login_error_massage() {
		login.validate_login_error_massage();
	}

}
