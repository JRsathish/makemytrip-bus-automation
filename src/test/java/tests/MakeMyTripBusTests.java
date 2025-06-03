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
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import pages.AvailableBusesPage;
import pages.BusSearchPage;
import pages.CheckOutPage;
import pages.HomePage;
import utils.ExcelUtils;
import utils.ExtentReportManager;

public class MakeMyTripBusTests {

    private WebDriver driver;
    private ExtentReports extent;
    private ExtentTest test;

    private HomePage homePage;
    private BusSearchPage busSearchPage;
    private AvailableBusesPage availableBusesPage;
    private CheckOutPage checkOutpage;

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://www.makemytrip.com/bus-tickets/");

        // Initialize Extent Report
        extent = ExtentReportManager.getInstance();

        // Initialize POM Pages
        homePage = new HomePage(driver);
        busSearchPage = new BusSearchPage(driver);
        availableBusesPage = new AvailableBusesPage(driver);
        checkOutpage = new CheckOutPage(driver);
    }

    @BeforeMethod
    public void resetTest(Method method) {
        // Create a new ExtentTest for each iteration
        test = extent.createTest("Test: " + method.getName());
    }

    @DataProvider(name = "bookingData")
    public Object[][] getData() {
        return ExcelUtils.getTestData("src/main/resources/TestData.xlsx", "TestData");
    }

    @AfterMethod
    public void captureFailureScreenshot(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            try {
                File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String path = "src/main/resources/ScreenShots/" + result.getName() + "_" + System.currentTimeMillis() + ".png";
                Files.copy(src.toPath(), Paths.get(path));
                test.addScreenCaptureFromPath(path);
                System.out.println("[INFO] Screenshot saved: " + path);
            } catch (IOException e) {
                System.out.println("[ERROR] Screenshot failed: " + e.getMessage());
            }
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
        ExtentReportManager.flushReports();
    }
    
    
    @Test(dataProvider = "bookingData")
   public void makeMyTripBusBookingFlow(String from, String to, String date, String name, String age, String gender, String email, String number) throws InterruptedException {

        test.info("Closing login popup if visible.");
        homePage.closeLoginPopupIfVisible();

        test.info("Entering 'From' city: " + from);
        busSearchPage.setFormCity(from);

        test.info("Entering 'To' city: " + to);
        busSearchPage.setToCity(to);

        test.info("Selecting journey date: " + date);
        busSearchPage.selectJourneyDate(date);

        test.info("Clicking search button.");
        busSearchPage.clickSearchButton();

        try {
            test.info("Retrieving available buses.");
            int count = availableBusesPage.getAvailableBuses();

            if (count != 0) {
                test.info("Selecting seat.");
                availableBusesPage.checkIfSeatisSelected();

                test.info("Filling passenger details.");
                checkOutpage.enterFormDetails(name, age, gender, email, number);

                test.pass("Booking flow completed for " + from + " to " + to);
            } else {
                test.warning("No buses found. Redirecting to homepage.");
            }

        } catch (Exception e) {
            test.fail("Booking failed from " + from + " to " + to + " on " + date + ". Reason: " + e.getMessage());
        } finally {
            // Always redirect to homepage after test
            driver.get("https://www.makemytrip.com/bus-tickets/");
        }
    }


}
