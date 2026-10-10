package com.integration;

import com.report.ReportManager;
import lombok.extern.slf4j.Slf4j;
import org.testng.IExecutionListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class TestNGReportListener implements ITestListener, IExecutionListener, IInvokedMethodListener {

    /**
     * Initializes the selected report before test execution begins.
     */
    @Override
    public void onExecutionStart() {
        ReportManager.initialize();
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
        log.info("{} test succeeded.", testName);
        ReportManager.onTestSuccess();
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
        ReportManager.onTestFailure(getFailureMessage(result));
    }

    /**
     * Handles test skipped.
     *
     * @param result TestNG test result
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = getTestName(result);
        log.info("{} test was skipped.", testName);
        ReportManager.onTestSkipped(getFailureMessage(result));
    }

    /**
     * Flushes the selected report after TestNG execution finishes.
     */
    @Override
    public void onExecutionFinish() {
        ReportManager.flush();
    }

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

    /**
     * Captures a screenshot immediately after a failed test method invocation.
     *
     * @param method invoked method
     * @param result TestNG test result
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod() && result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot(getTestName(result));
        }
    }
}
