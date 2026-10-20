package com.integration;

import com.report.ReportManager;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import lombok.extern.slf4j.Slf4j;
import org.testng.IExecutionListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class TestNGReportListener implements ITestListener, IExecutionListener, TestLifecycleListener {

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
        return result.getMethod().getConstructorOrMethod().getName();
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
     * Attaches a screenshot before a failed test stops.
     *
     * @param result TestNG test result
     */
    @Override
    public void beforeTestStop(TestResult result) {
        if (result == null) {
            return;
        }
        Status status = result.getStatus();
        if (status != Status.FAILED) {
            return;
        }
        String testName = result.getName();
        takeScreenshot(testName);
    }
}
