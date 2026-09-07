package com.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.constant.Constant;
import com.driver.DriverManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.utilities.JsonHelper;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class ExtentReport implements Report {

    private final ExtentReports extentReports;
    private final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    /**
     * Creates an ExtentReport instance.
     *
     * @throws IOException if the Extent configuration cannot be loaded
     */
    public ExtentReport() throws IOException {
        JsonObject config = JsonHelper.getData(
                Constant.EXTENT_REPORT_CONFIG_PATH,
                JsonObject.class
        );
        String reportPath = config.get("reportPath").getAsString();
        if (reportPath.isBlank()) {
            throw new IllegalArgumentException("'reportPath' cannot be empty in: " + Constant.EXTENT_REPORT_CONFIG_PATH);
        }
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.loadJSONConfig(new File(Constant.EXTENT_REPORT_CONFIG_PATH));
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
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
