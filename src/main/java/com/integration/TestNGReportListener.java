package com.integration;

import com.report.ReportManager;
import lombok.extern.slf4j.Slf4j;
import org.testng.IExecutionListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class TestNGReportListener implements ITestListener, IExecutionListener {

    /**
     * Initializes the configured report for the current TestNG test.
     *
     * @param context TestNG test context
     */
    @Override
    public void onStart(ITestContext context) {
        ReportManager.initialize();
        log.info("TestNG report initialized.");
    }

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
        ReportManager.finishTest();
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
        takeScreenshot(testName);
        ReportManager.fail(getFailureMessage(result));
        ReportManager.finishTest();
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
        ReportManager.finishTest();
    }

    /**
     * Flushes reports after the entire TestNG execution finishes.
     */
    @Override
    public void onExecutionFinish() {
        ReportManager.flush();
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
            ReportManager.attachScreenshot(testName + " - Failure");
        } catch (Exception e) {
            log.error("Unable to capture failure screenshot: {}", e.getMessage());
        }
    }
}
