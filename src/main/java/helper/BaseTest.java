package helper;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class BaseTest {

	WebDriver driver;

	public void selectValueFromVisibleText(WebElement ele, String Text) {
		Select s = new Select(ele);
		s.selectByValue(Text);
	}

	public void clickOnElement(WebElement ele) {
		try {
			ele.click();
		} catch (Exception e) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click()", ele);
		}
	}

	public void clearAndText(WebElement ele, String inputText) {
		ele.clear();
		ele.sendKeys(inputText);
	}

	public void mouseHover(WebElement ele) {
		Actions a = new Actions(driver);
		a.moveToElement(ele).perform();
	}

	public void selectValueFromBootStrapDropdown(List<WebElement> list, String value) {
		for (WebElement ele : list) {
			String text = ele.getText();

			if (text.equals(value)) {
				clickOnElement(ele);
				break;
			}
		}

	}

}
