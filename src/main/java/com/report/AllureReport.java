package com.report;

import com.driver.DriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;

@Slf4j
public class AllureReport implements Report, TestLifecycleListener {

    /**
     * Returns the report name used by the report provider.
     */
    @Override
    public String getName() {
        return "allure";
    }

    /**
     * Starts a new Allure test case.
     */
    @Override
    public void startTest(String testName) {
    }

    /**
     * Logs an informational message.
     *
     * @param message message to log
     */
    @Override
    public void info(String message) {
        Allure.step(message);
    }

    /**
     * Logs a passed message.
     *
     * @param message message to log
     */
    @Override
    public void pass(String message) {
        Allure.step(message, Status.PASSED);
    }

    /**
     * Logs a failed message.
     *
     * @param message message to log
     */
    @Override
    public void fail(String message) {
        Allure.step(message, Status.FAILED);
    }

    /**
     * Logs a skipped message.
     *
     * @param message message to log
     */
    @Override
    public void skip(String message) {
        log.debug("AllureReport.skip called: {}", message);
        Allure.step(message, Status.SKIPPED);
    }

    /**
     * Attaches a screenshot to the Allure report using DriverManager.
     *
     * @param name screenshot name
     */
    public void attachScreenshot(String name) {
        byte[] screenshot = DriverManager.getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), ".png");
    }

    /**
     * AllureTestNg manages successful test status.
     */
    @Override
    public void onTestSuccess() {
    }

    /**
     * AllureTestNg manages failed test status.
     *
     * @param message failure message
     */
    @Override
    public void onTestFailure(String message) {
    }

    /**
     * AllureTestNg manages skipped test status.
     *
     * @param message skip message
     */
    @Override
    public void onTestSkipped(String message) {
    }

    /**
     * Allure writes the result when the test is finished.
     */
    @Override
    public void flush() {
        // No explicit flush is required.
    }
}
