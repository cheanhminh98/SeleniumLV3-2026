package com.report;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

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
        for (Report report : reports) {
            report.startTest(testName);
        }
    }

    /**
     * Logs an informational message to all reports.
     *
     * @param message message to log
     */
    public static void info(String message) {
        for (Report report : reports) {
            report.info(message);
        }
    }

    /**
     * Logs a passed message to all reports.
     *
     * @param message message to log
     */
    public static void pass(String message) {
        for (Report report : reports) {
            report.pass(message);
        }
    }

    /**
     * Logs a failed message to all reports.
     *
     * @param message message to log
     */
    public static void fail(String message) {
        for (Report report : reports) {
            report.fail(message);
        }
    }

    /**
     * Logs a skipped message to all reports.
     *
     * @param message message to log
     */
    public static void skip(String message) {
        for (Report report : reports) {
            report.skip(message);
        }
    }

    /**
     * Attaches a screenshot to all reports.
     *
     * @param driver WebDriver instance
     * @param name screenshot name
     */
    public static void attachScreenshot(WebDriver driver, String name) {
        for (Report report : reports) {
            report.attachScreenshot(driver, name);
        }
    }
}
