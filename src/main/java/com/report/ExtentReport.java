package com.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.driver.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ExtentReport implements Report {

    private static final String DEFAULT_REPORT_PATH = "target/extent-report/ExtentReport.html";
    private final ExtentReports extentReports;
    private final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    /**
     * Creates an ExtentReport instance using the default report path.
     */
    public ExtentReport() {
        this(DEFAULT_REPORT_PATH);
    }

    /**
     * Creates an ExtentReport instance using the specified report path.
     *
     * @param reportPath path of the Extent report
     */
    public ExtentReport(String reportPath) {
        if (reportPath == null || reportPath.isBlank()) {
            throw new IllegalArgumentException("Extent report path cannot be null or empty.");
        }
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        registerShutdownHook();
    }

    /**
     * Starts an Extent test.
     *
     * @param testName test name
     */
    @Override
    public void startTest(String testName) {
        extentTest.set(extentReports.createTest(testName));
    }

    /**
     * Logs an informational message.
     *
     * @param message message to log
     */
    @Override
    public void info(String message) {
        extentTest.get().log(Status.INFO, message);
    }

    /**
     * Logs a passed message.
     *
     * @param message message to log
     */
    @Override
    public void pass(String message) {
        extentTest.get().log(Status.PASS, message);
    }

    /**
     * Logs a failed message.
     *
     * @param message message to log
     */
    @Override
    public void fail(String message) {
        extentTest.get().log(Status.FAIL, message);
    }

    /**
     * Logs a skipped message.
     *
     * @param message message to log
     */
    @Override
    public void skip(String message) {
        extentTest.get().log(Status.SKIP, message);
    }

    /**
     * Captures and attaches a screenshot to ExtentReports.
     *
     * @param driver WebDriver used by the test
     * @param name screenshot name
     */
    @Override
    public void attachScreenshot(WebDriver driver, String name) {
        if (!(driver instanceof TakesScreenshot)) {
            return;
        }
        String screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        extentTest.get().addScreenCaptureFromBase64String(screenshot, name);
    }

    /**
     * Attaches a screenshot to the ExtentReports report using DriverManager.
     *
     * @param driverManager driver manager
     * @param name screenshot name
     */
    @Override
    public void attachScreenshot(DriverManager driverManager, String name) {
        if (driverManager == null) {
            return;
        }
        attachScreenshot(DriverManager.getDriver(), name);
    }

    /**
     * Ends the current Extent test.
     */
    public void endTest() {
        extentTest.remove();
    }

    /**
     * Flushes ExtentReports.
     */
    public void flush() {
        extentReports.flush();
    }

    /**
     * Gets the current Extent test.
     *
     * @return current ExtentTest
     * @throws IllegalStateException if no test is active
     */
    private ExtentTest getCurrentTest() {
        ExtentTest test = extentTest.get();
        if (test == null) {
            throw new IllegalStateException("No active Extent test. " +
                            "startTest() must be called before logging.");
        }
        return test;
    }

    /**
     * Registers a JVM shutdown hook to flush ExtentReports.
     */
    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(extentReports::flush));
    }
}
