package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashSet;
import java.util.Set;

public class ExtentTestManager {

    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ExtentReports extent = ExtentManager.getReporter();

    // Keep track of scenarios already started
    private static Set<String> startedScenarios = new HashSet<>();

    public static ExtentTest getTest() {
        return test.get();
    }

    // Use scenarioId to prevent duplicates
    public static synchronized void startTest(String testName, String scenarioId) {
        if (startedScenarios.contains(scenarioId)) {
            // Skip duplicate scenario
            return;
        }
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
        startedScenarios.add(scenarioId);
    }

    public static void endTest() {
        test.remove();
    }
}
