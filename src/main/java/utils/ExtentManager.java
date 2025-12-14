package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

	private static ExtentReports extent;

	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/target/reports/Automation.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setDocumentTitle("Automation Report");
			spark.config().setReportName("Test Executation Report");

			extent = new ExtentReports();
			extent.attachReporter(spark);

			extent.setSystemInfo("OS", System.getProperty("os.name"));
			extent.setSystemInfo("Project Name", "hafeleindia");
			extent.setSystemInfo("Module / Feature", "All");
			extent.setSystemInfo("Build / Release Version", "1.1");
			extent.setSystemInfo("Test Cycle", "1.1");
			extent.setSystemInfo("Environment", "QA");
			extent.setSystemInfo("Tester", "Vinod");
		}
		return extent;
	}
}
