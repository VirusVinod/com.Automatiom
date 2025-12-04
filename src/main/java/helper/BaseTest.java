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

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

	public static Properties prop;
	public static WebDriver driver;
	static long timeout = 10;

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
	public void Setup() {
		String browserName = prop.getProperty("browser");
		if (browserName.equals("chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions option = new ChromeOptions();
			option.addArguments("--incognito");
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
	public void tearDown() {
		driver.quit();
	}
	

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

}
