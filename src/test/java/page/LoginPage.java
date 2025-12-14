package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import helper.BaseTest;

public class LoginPage extends BaseTest {

	public void user_enter_and(String uname, String pass) {

		WebElement username = driver.findElement(By.id("user-name"));
		clearAndText(username, uname);

		WebElement password = driver.findElement(By.id("password"));
		clearAndText(password, pass);
	}

	public void user_click_on_login_button() {
		WebElement loginbtn = driver.findElement(By.id("login-button"));
		clickOnElement(loginbtn);

	}

	public void validate_user_logged_in_sucessfully() {
		WebElement title = driver.findElement(By.xpath("//span[@class='title']"));
		softAssertTrue(title.getText().equals("Products"), "Products title not displayed");

	}

	public void validate_login_error_massage() {
		WebElement Errormessage = driver.findElement(By.xpath("//div[@class='error-message-container error']//h3"));
		softAssertTrue(
				Errormessage.getText()
						.equals("Epic sadface: Username and password do not match any user in this service"),
				"Error message is incorrect");
	}

}
