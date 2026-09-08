package com.report;

import com.driver.DriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

import java.io.ByteArrayInputStream;

public class AllureReport implements Report {

    /**
     * Starts an Allure test.
     *
     * @param testName test name
     */
    @Override
    public void startTest(String testName) {
        Allure.getLifecycle().updateTestCase(testResult -> testResult.setName(testName));
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
        Allure.step(message, Status.SKIPPED);
    }

    /**
     * Attaches a screenshot to the Allure report using DriverManager.
     *
     * @param name  screenshot name
     */
    @Override
    public void attachScreenshot(String name) {
        byte[] screenshot = DriverManager.captureScreen();
        Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), ".png");
    }
}
