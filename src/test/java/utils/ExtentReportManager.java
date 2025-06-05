package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance("reports/BusBookingExtentReport.html");
        }
        return extent;
    }

    public static ExtentReports createInstance(String filePath) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(filePath);
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Bus Ticket Booking Automation Report");
        sparkReporter.config().setReportName("Booking Flow Tests");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Project", "Bus Ticket Booking");
        extent.setSystemInfo("Tester", "Sathish Kumar JR");
        extent.setSystemInfo("Environment", "QA");

        return extent;
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}