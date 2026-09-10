package com.report;

public interface Report {

    /**
     * Starts a test.
     *
     * @param testName test name
     */
    void startTest(String testName);

    /**
     * Logs an informational message.
     *
     * @param message message to log
     */
    void info(String message);

    /**
     * Logs a passed test message.
     *
     * @param message message to log
     */
    void pass(String message);

    /**
     * Logs a failed test message.
     *
     * @param message message to log
     */
    void fail(String message);

    /**
     * Logs a skipped test message.
     *
     * @param message message to log
     */
    void skip(String message);

    /**
     * Attaches a screenshot to the report.
     *
     * @param name  screenshot name
     */
    void attachScreenshot(String name);
}
