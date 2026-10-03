package com.report;

import com.driver.DriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.AllureResultsWriter;
import io.qameta.allure.FileSystemResultsWriter;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.TestResult;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.UUID;

@Slf4j
public class AllureReport implements Report {

    private final AllureLifecycle lifecycle;
    private final ThreadLocal<String> testUuid = new ThreadLocal<>();

    /**
     * Creates an Allure report using the configured results directory.
     */
    public AllureReport() {
        String resultsDirectory = getResultsDirectory();
        AllureResultsWriter writer = new FileSystemResultsWriter(new File(resultsDirectory).toPath());
        lifecycle = new AllureLifecycle(writer);
        Allure.setLifecycle(lifecycle);
    }

    /**
     * Returns the configured Allure results directory.
     */
    private String getResultsDirectory() {
        String directory = System.getProperty("allure.results.directory");
        if (directory == null || directory.isBlank()) {
            directory = "allure-results";
        }
        return directory;
    }

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
        String uuid = UUID.randomUUID().toString();
        TestResult testResult = new TestResult().setUuid(uuid).setName(testName);
        testUuid.set(uuid);
        lifecycle.scheduleTestCase(uuid, testResult);
        lifecycle.startTestCase(uuid);
    }

    /**
     * Adds an information step to the current test.
     */
    @Override
    public void info(String message) {
        String testCaseUuid = getTestUuid();
        String stepUuid = UUID.randomUUID().toString();
        StepResult stepResult = new StepResult().setName(message);
        lifecycle.startStep(testCaseUuid, stepUuid, stepResult);
        lifecycle.stopStep(stepUuid);
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
     * Captures and attaches a screenshot to the current test.
     */
    @Override
    public void attachScreenshot(String name) {
        byte[] screenshot = DriverManager.getScreenshotAs(OutputType.BYTES);
        lifecycle.addAttachment(name, "image/png", ".png", new ByteArrayInputStream(screenshot));
    }

    /**
     * Allure writes the result when the test is finished.
     */
    @Override
    public void flush() {
        // No explicit flush is required.
    }

    /**
     * Finishes the current test with the given status.
     */
    @Override
    public void finishTest() {
        String uuid = getTestUuid();
        lifecycle.stopTestCase(uuid);
        lifecycle.writeTestCase(uuid);
        testUuid.remove();
    }

    /**
     * Returns the UUID of the current test.
     */
    private String getTestUuid() {
        String uuid = testUuid.get();
        if (uuid == null) {
            throw new IllegalStateException("No active Allure test. " + "startTest() must be called before logging.");
        }
        return uuid;
    }
}
