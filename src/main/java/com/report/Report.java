package com.report;

import com.driver.DriverManager;
import org.openqa.selenium.WebDriver;

public interface Report {

    /**
     * Starts a test.
     *
     * @param testName test name
     */
    void startTest(String testName);

    /**
     * Logs an informational message.
     *
     * @param message message to log
     */
    void info(String message);

    /**
     * Logs a passed test message.
     *
     * @param message message to log
     */
    void pass(String message);

    /**
     * Logs a failed test message.
     *
     * @param message message to log
     */
    void fail(String message);

    /**
     * Logs a skipped test message.
     *
     * @param message message to log
     */
    void skip(String message);

    /**
     * Attaches a screenshot to the report.
     *
     * @param driver WebDriver used by the test
     * @param name screenshot name
     */
    void attachScreenshot(WebDriver driver, String name);

    void attachScreenshot(DriverManager driverManager, String name);
}
