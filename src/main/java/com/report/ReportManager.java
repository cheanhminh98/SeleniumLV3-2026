package com.report;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;

@Slf4j
public class ReportManager {

    private static final List<Report> reports = new ArrayList<>();
    private static boolean initialized = false;

    /**
     * Initializes the report using the system property.
     * If no system property is provided, Allure is used by default.
     */
    public static synchronized void initialize() {
        initialize(null);
    }

    /**
     * Initializes the configured report.
     * System property has higher priority than the report configuration.
     * If no system property is provided, Allure is used by default.
     */
    public static synchronized void initialize(String configuredReport) {
        if (initialized) {
            return;
        }
        String report = validateReport(System.getProperty("report"), configuredReport);
        Report selectedReport = findReport(report);
        register(selectedReport);
        initialized = true;
        log.info("Report initialized: {}", selectedReport.getName());
    }

    /**
     * Validates the report configuration.
     *
     * @param configuredReport the configured report name
     * @return the valid report name
     */
    private static String validateReport(String systemReport, String configuredReport) {
        if (systemReport != null && !systemReport.isBlank()) {
            return systemReport;
        }
        if (configuredReport != null && !configuredReport.isBlank()) {
            return configuredReport;
        }
        return "allure";
    }

    /**
     * Finds a report implementation using ServiceLoader.
     *
     * @param reportName report name
     * @return matching report implementation
     */
    private static Report findReport(String reportName) {
        String normalizedReport = reportName.trim().toLowerCase();
        ServiceLoader<Report> loader = ServiceLoader.load(Report.class);
        for (Report report : loader) {
            if (report.getName().equalsIgnoreCase(normalizedReport)) {
                return report;
            }
        }
        throw new IllegalArgumentException("Unsupported report: " + reportName);
    }

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
     * Attaches a screenshot to all reports using DriverManager.
     *
     * @param name screenshot name
     */
    public static void attachScreenshot(String name) {
        executeForEachReport(report -> report.attachScreenshot(name));
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
