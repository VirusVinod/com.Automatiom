package helper;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

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
			js.executeScript("arguments[0].scrollIntoView(true)", ele);
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

	public void switchToFrame(String id) {
		driver.switchTo().frame(id);
	}

	public void handleAlert() {
		Alert a = driver.switchTo().alert();
		a.accept();

	}

	public void takeScreenshot1(String Ssname) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		File dest = new File(System.getProperty("user.dir") + "/Screenshot/" + Ssname + ".png");
		FileHandler.copy(src, dest);
	}

	public WebElement waitForElement(WebElement ele, long timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.visibilityOf(ele));
	}

}














