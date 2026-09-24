package com.report;

import com.driver.DriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.AllureResultsWriter;
import io.qameta.allure.FileSystemResultsWriter;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
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
     * Creates an Allure report.
     */
    public AllureReport() {
        String resultsDirectory = getResultsDirectory();
        AllureResultsWriter writer =
                new FileSystemResultsWriter(
                        new File(resultsDirectory).toPath()
                );
        lifecycle = new AllureLifecycle(writer);
        Allure.setLifecycle(lifecycle);
    }

    /**
     * Gets the Allure results directory.
     *
     * @return results directory
     */
    private String getResultsDirectory() {
        String directory = System.getProperty("allure.results.directory");
        if (directory == null || directory.isBlank()) {
            directory = "allure-results";
        }
        return directory;
    }

    /**
     * Starts an Allure test.
     *
     * @param testName test name
     */
    @Override
    public void startTest(String testName) {
        String uuid = UUID.randomUUID().toString();
        TestResult testResult = new TestResult()
                .setUuid(uuid)
                .setName(testName);
        testUuid.set(uuid);

        log.info("ALLURE START: " + testName);
        log.info("ALLURE UUID: " + uuid);
        log.info("ALLURE RESULTS: " + getResultsDirectory());

        lifecycle.scheduleTestCase(uuid, testResult);
        lifecycle.startTestCase(uuid);
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
        finishTest(Status.PASSED, null);
    }

    /**
     * Logs a failed message.
     *
     * @param message message to log
     */
    @Override
    public void fail(String message) {
        Allure.step(message);
        StatusDetails statusDetails = new StatusDetails().setMessage(message);
        finishTest(Status.FAILED, statusDetails);
    }

    /**
     * Logs a skipped message.
     *
     * @param message message to log
     */
    @Override
    public void skip(String message) {
        Allure.step(message);
        StatusDetails statusDetails = new StatusDetails()
                .setMessage(message);
        finishTest(Status.SKIPPED, statusDetails);
    }

    /**
     * Attaches a screenshot to the Allure report using DriverManager.
     *
     * @param name  screenshot name
     */
    @Override
    public void attachScreenshot(String name) {
        byte[] screenshot = DriverManager.getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), ".png");
    }

    /**
     * Finishes and writes the current Allure test.
     *
     * @param status test status
     * @param statusDetails test status details
     */
    private void finishTest(Status status, StatusDetails statusDetails) {
        String uuid = testUuid.get();
        log.info("ALLURE FINISH UUID: " + uuid);
        if (uuid == null) {
            return;
        }
        lifecycle.updateTestCase(uuid,
                testResult -> {
                    testResult.setStatus(status);
                    if (statusDetails != null) {
                        testResult.setStatusDetails(statusDetails);
                    }
                }
        );
        lifecycle.stopTestCase(uuid);
        lifecycle.writeTestCase(uuid);
        testUuid.remove();
    }
}
