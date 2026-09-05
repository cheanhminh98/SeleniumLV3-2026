package com.report;

import com.driver.DriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class ReportManager {

    private static final List<Report> reports = new ArrayList<>();

    /**
     * Registers a reporting implementation.
     *
     * @param report report implementation
     */
    public static void register(Report report) {
        if (report == null) {
            throw new IllegalArgumentException("Report implementation cannot be null.");
        }
        reports.add(report);
    }

    /**
     * Removes all registered reports.
     */
    public static void clear() {
        reports.clear();
    }

    /**
     * Starts a test in all registered reports.
     *
     * @param testName test name
     */
    public static void startTest(String testName) {
        executeForEachReport(report -> report.startTest(testName));
    }

    /**
     * Logs an informational message to all reports.
     *
     * @param message message to log
     */
    public static void info(String message) {
        executeForEachReport(report -> report.info(message));
    }

    /**
     * Logs a passed message to all reports.
     *
     * @param message message to log
     */
    public static void pass(String message) {
        executeForEachReport(report -> report.pass(message));
    }

    /**
     * Logs a failed message to all reports.
     *
     * @param message message to log
     */
    public static void fail(String message) {
        executeForEachReport(report -> report.fail(message));
    }

    /**
     * Logs a skipped message to all reports.
     *
     * @param message message to log
     */
    public static void skip(String message) {
        executeForEachReport(report -> report.skip(message));
    }

    /**
     * Attaches a screenshot to all reports.
     *
     * @param driver WebDriver instance
     * @param name screenshot name
     */
    public static void attachScreenshot(WebDriver driver, String name) {
        executeForEachReport(report -> report.attachScreenshot(driver, name));
    }

    /**
     * Attaches a screenshot to all reports using DriverManager.
     *
     * @param driverManager driver manager
     * @param name screenshot name
     */
    public static void attachScreenshot(DriverManager driverManager, String name) {
        executeForEachReport(report -> report.attachScreenshot(driverManager, name));
    }

    /**
     * Runs the action for each report.
     * If one report fails, the other reports continue to run
     *
     * @param action action to run
     */
    private static void executeForEachReport(Consumer<Report> action) {
        for (Report report : reports) {
            try {
                action.accept(report);
            } catch (Exception e) {
                log.error(
                        "Report execution failed for {}: {}",
                        report.getClass().getSimpleName(),
                        e.getMessage()
                );
            }
        }
    }
}
