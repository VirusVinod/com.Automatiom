package page;

import org.openqa.selenium.By;

import helper.Base;

public class HomePage extends Base {

	By BtnAcceptCookies = By.xpath("//button[@id='onetrust-accept-btn-handler']");
	By txtSearch = By.xpath("//input[@id='inputSearchTerm']");
	By clickSearchIcon = By.xpath("//button[@data-searchid='searchButton']");

	public void clickOnAcceptCookies() {
		clickOnElement(BtnAcceptCookies);
	}

	public void searchProduct(String validProduct) {
		clearAndEnter(txtSearch, validProduct);
		clickOnElement(clickSearchIcon);
	}
	

}
