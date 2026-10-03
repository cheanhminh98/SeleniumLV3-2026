package com.report;

import com.constant.Constant;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.function.Consumer;

@Slf4j
public class ReportManager {

    private static final String REPORT_PROPERTY = "report";
    private static final String DEFAULT_REPORT = "allure";

    private static Report report;

    /**
     * Initializes the configured report.
     * System property has higher priority than the properties file.
     * If no system property is provided, Allure is used by default.
     */
    public static synchronized void initialize() {
        if (report != null) {
            return;
        }
        String reportName = getConfiguredReport();
        report = findReport(reportName);
        log.info("Report initialized: {}", report.getName());
    }

    /**
     * Gets the configured report.
     * System property has higher priority than the properties file.
     *
     * @return configured report name
     */
    private static String getConfiguredReport() {
        String systemReport = System.getProperty(REPORT_PROPERTY);
        if (systemReport != null && !systemReport.isBlank()) {
            return systemReport.trim();
        }
        String configuredReport = loadReportProperty();
        if (configuredReport != null && !configuredReport.isBlank()) {
            return configuredReport.trim();
        }
        return DEFAULT_REPORT;
    }

    /**
     * Loads the report configuration from the properties file.
     *
     * @return configured report name, or null if not configured
     */
    private static String loadReportProperty() {
        Properties properties = new Properties();
        try (InputStream inputStream = ReportManager.class.getClassLoader()
                .getResourceAsStream(Constant.REPORT_CONFIG_PATH)) {
            if (inputStream == null) {
                return null;
            }
            properties.load(inputStream);
            return properties.getProperty(REPORT_PROPERTY);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load report configuration.", e);
        }
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
     * Gets the initialized report.
     *
     * @return initialized report
     */
    private static synchronized Report getReport() {
        if (report == null) {
            throw new IllegalStateException("Report has not been initialized. "
                    + "Call ReportManager.initialize() before using the report.");
        }
        return report;
    }

    /**
     * Starts a test.
     *
     * @param testName test name
     */
    public static void startTest(String testName) {
        execute(report -> report.startTest(testName));
    }

    /**
     * Logs an informational message.
     *
     * @param message message to log
     */
    public static void info(String message) {
        execute(report -> report.info(message));
    }

    /**
     * Logs a passed test message.
     *
     * @param message message to log
     */
    public static void pass(String message) {
        execute(report -> report.pass(message));
    }

    /**
     * Logs a failed test message.
     *
     * @param message message to log
     */
    public static void fail(String message) {
        execute(report -> report.fail(message));
    }

    /**
     * Logs a skipped test message.
     *
     * @param message message to log
     */
    public static void skip(String message) {
        execute(report -> report.skip(message));
    }

    /**
     * Attaches a screenshot to the current test.
     *
     * @param name screenshot name
     */
    public static void attachScreenshot(String name) {
        execute(report -> report.attachScreenshot(name));
    }

    /**
     * Executes an action on the configured report.
     * Report failures are logged and do not interrupt test execution.
     *
     * @param action report action
     */
    private static void execute(Consumer<Report> action) {
        Report currentReport = getReport();
        try {
            action.accept(currentReport);
        } catch (Exception e) {
            log.error("Report execution failed for {}: {}", currentReport.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Flushes the configured report.
     */
    public static void flush() {
        Report currentReport = getReport();
        try {
            currentReport.flush();
        } catch (Exception e) {
            log.error("Unable to flush report {}: {}", currentReport.getName(), e.getMessage(), e);
        }
    }

    /**
     * Finishes the current test.
     */
    public static void finishTest() {
        execute(Report::finishTest);
    }
}
