package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/java/features", glue = { "stepDefination",
		"helper" }, tags = "@Reg", plugin = "json:target/reports/json.html", dryRun = false)
public class TestRunner extends AbstractTestNGCucumberTests {

}