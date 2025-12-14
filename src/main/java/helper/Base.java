package helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import utils.Log;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ExtentManager;
import utils.ExtentTestManager;

public class Base {

	public static Properties prop;
	public static WebDriver driver;
	static long timeout = 10;
	SoftAssert soft = new SoftAssert();

	static {
		FileInputStream file;
		try {
			file = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/resources/env.properties");
			prop = new Properties();
			prop.load(file);
		} catch (IOException e) {
			Log.error("File Not Supported or Not Found");
		}
	}

//	@Before
//	public void beforeScenario() {
//		Setup();
//	}
	@Before
	public void beforeScenario(Scenario scenario) {
		Setup();
		ExtentTestManager.startTest(scenario.getName(), scenario.getUri().toString());
	}

	public void Setup() {
		String browserName = prop.getProperty("browser");
		if (browserName.equals("chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions option = new ChromeOptions();
			option.addArguments("--incognito");
			option.addArguments("--disable-notifications");
			option.addArguments("--start-maximized");
			option.addArguments("--disable-extensions");
			option.addArguments("--ignore-certificate-errors");
			option.addArguments("--disable-infobars");
			option.addArguments("--disable-gpu");
			option.addArguments("--no-sandbox");
			option.addArguments("--disable-dev-shm-usage");
			option.addArguments("--headless");
			driver = new ChromeDriver(option);
		} else if (browserName.equals("firefox")) {
			FirefoxOptions option = new FirefoxOptions();
			option.addArguments("--incognito");
			driver = new FirefoxDriver(option);
		} else if (browserName.equals("edge")) {
			EdgeOptions opetion = new EdgeOptions();
			opetion.addArguments("--incognito");
			driver = new EdgeDriver(opetion);
		}

		driver.get(prop.getProperty("siteUrl"));
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Long.parseLong(prop.getProperty("timeouts"))));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(prop.getProperty("timeouts"))));

	}

//	@After
//	public void afterScenario(Scenario scenario) {
//		if (scenario.isFailed()) {
//			try {
//				String path = takeScreenshot(scenario.getName());
//				ExtentTestManager.getTest().fail("Test Failed - Screenshot Attached").addScreenCaptureFromPath(path);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		ExtentTestManager.endTest();
//
//		if (driver != null) {
//			driver.quit();
//		}
//	}
	@After
	public void afterScenario(Scenario scenario) {
		if (scenario.isFailed()) {
			try {
				String path = takeScreenshot(scenario.getName());
				ExtentTestManager.getTest().fail("Test Failed - Screenshot Attached").addScreenCaptureFromPath(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ExtentTestManager.endTest();
		if (driver != null) {
			driver.quit();
		}
	}

//	@AfterAll
//	public static void afterAll() {
//		ExtentManager.getReporter().flush();
//		System.out.println("After all Scenario - flush report");
//	}

	@AfterAll
	public static void afterAll() {
		ExtentManager.getReporter().flush();
	}

	public void selectValueFromVisibleText(By locator, String text, String type) {
		WebElement ele = waitForElement(locator, 10);
		Select s = new Select(ele);

		switch (type.toLowerCase().trim()) {
		case "visibletext":
		case "visible":
		case "text":
			s.selectByVisibleText(text);
			break;

		case "value":
			s.selectByValue(text);
			break;

		case "index":
			try {
				s.selectByIndex(Integer.parseInt(text));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Index must be a number. Provided: " + text);
			}
			break;

		default:
			throw new IllegalArgumentException("Invalid select type: " + type + ". Use visibleText, value, or index.");
		}
	}

	public void clickOnElement(By locator) {
		try {
			driver.findElement(locator).click();
		} catch (Exception e) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true)", locator);
			js.executeScript("arguments[0].click()", waitForElement(locator, 20));
		}
	}

	public void clearAndEnter(By locator, String text) {
		WebElement ele = waitForElement(locator);
		ele.clear();
		ele.click();
		ele.sendKeys(text);
	}

	public String getText(By locattor) {
		return driver.findElement(locattor).getText();
	}

	public void clearAndText(By locator, String inputText) {
		WebElement ele = driver.findElement(locator);
		ele.clear();
		ele.sendKeys(inputText);
	}

	public void mouseHover(By locator) {
		WebElement ele = driver.findElement(locator);
		Actions a = new Actions(driver);
		a.moveToElement(ele).perform();
	}

	public void selectValueFromBootStrapDropdown(By locator, String value) {
		List<WebElement> list = driver.findElements(locator);
		for (WebElement ele : list) {
			if (ele.getText().trim().equals(value)) {
				ele.click();
				break;
			}
		}
	}

	public void selectDropdownByVisibleText(By locator, String value) {
		Select s = new Select(driver.findElement(locator));
		s.selectByVisibleText(value);
	}

	public boolean isDisplayed(By locator) {
		try {
			return driver.findElement(locator).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public void switchToFrame(String id) {
		driver.switchTo().frame(id);
	}

	public void handleAlert(String action) {
		Alert a = driver.switchTo().alert();

		switch (action) {
		case "accept":
			a.accept();
			break;
		case "dismiss()":
			a.dismiss();
			break;
		default:
			throw new IllegalArgumentException("Invalid Alert type");

		}

	}

	public String takeScreenshot(String Ssname) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir") + "/Screenshot/" + Ssname + ".png";
		File dest = new File(path);
		FileHandler.copy(src, dest);
		return path; // <-- return the path
	}

	public WebElement waitForElement(By locator, long timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public WebElement waitForElement(By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void switchWindow(String title) {

		Set<String> allWindows = driver.getWindowHandles();

		for (String window : allWindows) {
			driver.switchTo().window(window);
			String actualTitle = driver.getTitle();

			if (actualTitle.contains(title)) {
				break;
			}
		}

	}

	public String getElementText(By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement ele = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		return ele.getText();
	}

	public void softAssertEquals(Object actual, Object expected, String messgae) {
		soft.assertEquals(actual, expected, messgae);
		soft.assertAll();

	}

	public void softAssertTrue(boolean condition, String messgae) {
		soft.assertTrue(condition, messgae);

	}

	public void hardAssertEquals(Object actual, Object expected, String messgae) {
		Assert.assertEquals(actual, expected, messgae);

	}

	public void hardAssertTrue(boolean condition, String messgae) {
		Assert.assertTrue(condition, messgae);

	}

}
