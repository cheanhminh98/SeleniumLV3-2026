package com.integration;

import com.driver.DriverManager;
import com.report.ReportManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class TestNGReportListener implements ITestListener {

    /**
     * Handles test start.
     *
     * @param result TestNG test result
     */
    @Override
    public void onTestStart(ITestResult result) {
        String testName = getTestName(result);
        log.info("{} test is starting.", testName);
        ReportManager.startTest(testName);
    }

    /**
     * Handles test success.
     *
     * @param result TestNG test result
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = getTestName(result);
        log.info("{} test is succeeded.", testName);
        ReportManager.pass("Test passed.");
    }

    /**
     * Handles test failure.
     *
     * @param result TestNG test result
     */
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = getTestName(result);
        log.error("{} test is failed.", testName);
        ReportManager.fail(getFailureMessage(result));
        takeScreenshot(testName);
    }

    /**
     * Handles test skipped.
     *
     * @param result TestNG test result
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = getTestName(result);
        log.info("{} test is skipped.", testName);
        ReportManager.skip(getFailureMessage(result));
    }

    /**
     * Gets the TestNG test name.
     *
     * @param result TestNG test result
     * @return test name
     */
    private String getTestName(ITestResult result) {
        return result
                .getMethod()
                .getConstructorOrMethod()
                .getName();
    }

    /**
     * Gets the failure message.
     *
     * @param result TestNG test result
     * @return failure message
     */
    private String getFailureMessage(ITestResult result) {
        Throwable throwable = result.getThrowable();
        if (throwable == null) {
            return "Test failed.";
        }
        return throwable.getMessage() != null ? throwable.getMessage() : throwable.toString();
    }

    /**
     * Captures a screenshot after test failure.
     *
     * @param testName test name
     */
    private void takeScreenshot(String testName) {
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver == null) {
                return;
            }
            ReportManager.attachScreenshot(driver, testName + " - Failure");
        } catch (Exception e) {
            log.error("Unable to capture failure screenshot: " + e.getMessage());
        }
    }
}
