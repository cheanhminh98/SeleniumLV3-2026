package com.integration;

import com.driver.DriverManager;
import com.report.ReportManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.WebDriver;

@Slf4j
public class JUnitReportExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    /**
     * Starts reporting when a test starts execution.
     *
     * @param context JUnit extension context
     */
    @Override
    public void beforeTestExecution(ExtensionContext context) {
        String testName = getTestName(context);
        log.info(testName + " test is starting.");
        ReportManager.startTest(testName);
    }

    /**
     * Handles the test result after the test method has finished
     * and before JUnit executes @AfterEach.
     *
     * @param context JUnit extension context
     */
    @Override
    public void afterTestExecution(ExtensionContext context) {
        String testName = getTestName(context);
        if (context.getExecutionException().isEmpty()) {
            log.info(testName + " test is succeeded.");
            ReportManager.pass("Test passed.");
            return;
        }
        Throwable cause = context.getExecutionException().get();
        log.error(testName + " test is failed.");
        ReportManager.fail(getFailureMessage(cause));
        takeScreenshot(testName);
    }

    /**
     * Gets the test display name.
     *
     * @param context JUnit extension context
     * @return test name
     */
    private String getTestName(ExtensionContext context) {
        return context.getDisplayName();
    }

    /**
     * Gets a readable failure message.
     *
     * @param throwable failure cause
     * @return failure message
     */
    private String getFailureMessage(Throwable throwable) {
        if (throwable == null) {
            return "Test failed.";
        }
        return throwable.getMessage() != null
                ? throwable.getMessage()
                : throwable.toString();
    }

    /**
     * Captures and attaches a failure screenshot.
     *
     * @param testName test name
     */
    private void takeScreenshot(String testName) {
        try {
            WebDriver webDriver = DriverManager.getDriver();
            if (webDriver == null) {
                log.error("Unable to capture failure screenshot: " + "WebDriver is not available.");
                return;
            }
            ReportManager.attachScreenshot(webDriver, testName + " - Failure");
        } catch (Exception e) {
            log.error("Unable to capture failure screenshot: " + e.getMessage());
        }
    }
}
