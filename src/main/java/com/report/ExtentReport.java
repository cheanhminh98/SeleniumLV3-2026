package com.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.constant.Constant;
import com.driver.DriverManager;
import com.google.gson.JsonObject;
import com.utilities.JsonHelper;

import java.io.*;
import java.util.Properties;

public class ExtentReport implements Report {

    private final ExtentReports extentReports;
    private final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    /**
     * Initializes ExtentReports.
     */
    public ExtentReport() {
        Properties properties = loadProperties();
        String reportPath = getReportPath(properties);
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
    }

    /**
     * Loads Extent report configuration from the properties file.
     *
     * @return loaded properties
     */
    private Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(
                        Constant.EXTENT_REPORT_CONFIG_PATH)) {
            if (inputStream == null) {
                throw new IllegalStateException(
                        "Extent report configuration file not found: " + Constant.EXTENT_REPORT_CONFIG_PATH);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Extent report configuration.", e);
        }
        return properties;
    }

    /**
     * Gets the Extent report output path.
     *
     * @param properties report configuration
     * @return report output path
     */
    private String getReportPath(Properties properties) {
        String reportPath = properties.getProperty("extent.reporter.spark.out");
        if (reportPath == null || reportPath.isBlank()) {
            throw new IllegalStateException("Property 'extent.reporter.spark.out' is not configured.");
        }
        return reportPath;
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
     * Attaches a screenshot to the ExtentReports report using DriverManager.
     *
     * @param name  screenshot name
     */
    @Override
    public void attachScreenshot(String name) {
        byte[] screenshot = DriverManager.captureScreen();
        String base64 = java.util.Base64.getEncoder().encodeToString(screenshot);
        getCurrentTest().addScreenCaptureFromBase64String(base64, name);
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
