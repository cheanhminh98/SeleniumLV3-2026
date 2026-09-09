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
        String reportPath = getReportPath();
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
    }

    /**
     * Gets the Extent report output path.
     *
     * @return report output path
     */
    private String getReportPath() {
        String reportPath = System.getProperty("extent.reporter.spark.out");
        if (reportPath == null || reportPath.isBlank()) {
            reportPath = "test-output/ExtentReport/index.html";
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
        getCurrentTest().addScreenCaptureFromBase64String(
                DriverManager.captureScreenAsBase64(),
                name);
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
}
