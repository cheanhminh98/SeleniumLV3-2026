package com.report;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

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
        Allure.step(message);
    }

    /**
     * Logs a failed message.
     *
     * @param message message to log
     */
    @Override
    public void fail(String message) {
        Allure.step(message);
    }

    /**
     * Logs a skipped message.
     *
     * @param message message to log
     */
    @Override
    public void skip(String message) {
        Allure.step(message);
    }

    /**
     * Captures and attaches a screenshot to Allure.
     *
     * @param driver WebDriver used by the test
     * @param name screenshot name
     */
    @Override
    public void attachScreenshot(WebDriver driver, String name) {
        if (!(driver instanceof TakesScreenshot)) {
            return;
        }
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), ".png");
    }
}
