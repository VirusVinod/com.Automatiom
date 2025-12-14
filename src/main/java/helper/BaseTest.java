package helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.Alert;
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

public class BaseTest {

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
			System.out.println("File not found");
		}
	}

	@Before
	public void beforeScenario(Scenario scenario) {
		Setup();
		ExtentTestManager.startTest(scenario.getName(), scenario.getId());

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
//				option.addArguments("--headless=new");
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

	@After
	public void afterScenario(Scenario scenario) {
		if (scenario.isFailed()) {
			try {
				String path = takeScreenshot(scenario.getName());
				ExtentTestManager.getTest(scenario.getId()).fail("Test Failed - Screenshot Attached")
						.addScreenCaptureFromPath(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ExtentManager.getReporter().flush();
		if (driver != null) {
			driver.quit();
			driver = null;
		}

		ExtentTestManager.flushReports();
	}

	@AfterAll
	public static void afterAll() {
		utils.ExtentManager.getReporter().flush();
	}

	public void selectValueFromVisibleText(WebElement ele, String text, String type) {
		Select s = new Select(ele);
		switch (type.toLowerCase()) {
		case "visibleText":
			s.selectByVisibleText(text);
			break;
		case "value":
			s.selectByValue(text);
			break;
		case "index":
			s.selectByIndex(Integer.parseInt(type));
			break;

		default:
			throw new IllegalArgumentException("Invalid select type");
		}

	}

	public void clearAndText(WebElement ele, String inputText) {
		ele.clear();
		ele.sendKeys(inputText);
	}

	public void clickOnElement(WebElement ele) {
		try {
			waitForElement(ele, 15).click();
		} catch (Exception e) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true)", ele);
			js.executeScript("arguments[0].click()", ele);
		}
	}

	public String getText(WebElement ele) {
		return ele.getText();
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

	public String takeScreenshot(String screenshotName) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir") + "/Screenshot/" + screenshotName + ".png";
		File dest = new File(path);
		FileHandler.copy(src, dest);
		return path;
	}

	public WebElement waitForElement(WebElement ele, long timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.visibilityOf(ele));

	}

	public WebElement waitForElement(WebElement ele) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		return wait.until(ExpectedConditions.visibilityOf(ele));
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

	public String getElementText(WebElement ele) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(ele));
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
