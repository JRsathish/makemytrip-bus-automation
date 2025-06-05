package tests;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import utils.ExcelUtils;
import utils.ExtentReportManager;

public class TestBase {

	protected WebDriver driver;
	protected ExtentReports extent;
	protected ExtentTest test;

	@BeforeClass
	public void setup() {
		driver = new EdgeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get("https://www.makemytrip.com/bus-tickets/");
		extent = ExtentReportManager.getInstance(); // Initialize ExtentReports
	}

	@BeforeMethod
	public void resetTest(Method method) {
		test = extent.createTest("Test: " + method.getName()); // Extent test per method
	}

	@DataProvider(name = "bookingData")
	public Object[][] getData() {
		return ExcelUtils.getTestData("src/main/resources/TestData.xlsx", "TestData");
	}

	@AfterMethod
	public void captureFailureScreenshot(ITestResult result) throws IOException {
		if (ITestResult.FAILURE == result.getStatus()) {
			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String methodName = result.getMethod().getMethodName();
			String path = "src/main/resources/ScreenShots/" + methodName + "_" + System.currentTimeMillis() + ".png";
			Files.copy(src.toPath(), Paths.get(path));
			test.addScreenCaptureFromPath(path);
			test.fail("Test failed: " + result.getThrowable());
			System.out.println("[INFO] Screenshot saved: " + path);
		}
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
		ExtentReportManager.flushReports(); // Finalize Extent Report
	}
}
