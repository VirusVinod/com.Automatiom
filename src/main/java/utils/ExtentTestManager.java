package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ExtentReports extent = ExtentManager.getReporter();

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void startTest(String testName, String description) {
        ExtentTest extentTest = extent.createTest(testName, description);
        test.set(extentTest);
    }

    public static void endTest() {
        extent.flush(); // Write all logs to the report
        test.remove();  // Remove the test from ThreadLocal
    }
}
