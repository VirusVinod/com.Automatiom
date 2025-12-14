package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {

	private static ExtentReports extent = ExtentManager.getReporter();

	// Map to store ExtentTest per scenario
	private static Map<String, ExtentTest> testMap = new HashMap<>();

	// Start test if not already started
	public static synchronized void startTest(String testName, String scenarioId) {
		if (!testMap.containsKey(scenarioId)) {
			ExtentTest test = extent.createTest(testName);
			testMap.put(scenarioId, test);
		}
	}

	// Get test by scenarioId
	public static synchronized ExtentTest getTest(String scenarioId) {
		return testMap.get(scenarioId);
	}

	// Optional: remove test after scenario
	public static synchronized void endTest(String scenarioId) {
		testMap.remove(scenarioId);
	}

	public static void flushReports() {
		extent.flush();
	}
}
