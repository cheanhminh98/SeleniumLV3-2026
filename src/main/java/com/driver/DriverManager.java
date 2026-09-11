package com.driver;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static final ThreadLocal<DriverContainer> driverContainer = new ThreadLocal<>();

    /**
     * Initializes the driver for the current thread.
     *
     * @param driverConfig driver configuration
     */
    public static void initialize(DriverConfig driverConfig) {
        if (driverConfig == null) {
            throw new IllegalArgumentException("DriverConfig cannot be null.");
        }
        driverContainer.set(new DriverContainer(driverConfig));
    }

    /**
     * Gets the driver container for the current thread.
     *
     * @return current DriverContainer
     */
    private static DriverContainer getDriverContainer() {
        DriverContainer driver = driverContainer.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver has not been initialized.");
        }
        return driver;
    }

    /**
     * Gets the current WebDriver.
     *
     * @return current WebDriver
     */
    public static WebDriver getDriver() {
        return getDriverContainer().getDriver();
    }

    /**
     * Gets the current DriverConfig.
     *
     * @return current DriverConfig
     */
    public static DriverConfig getConfig() {
        return getDriverContainer().getConfig();
    }


    /**
     * Quits the WebDriver.
     */
    public static void quitDriver() {
        WebDriver webDriver = getDriver();
        if (webDriver != null) {
            try {
                webDriver.quit();
            } finally {
                driverContainer.remove();
            }
        }
    }

    /**
     * Opens the URL.
     *
     * @param url to open
     */
    public static void open(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL cannot be null or empty.");
        }
        getDriver().navigate().to(url);
    }

    /**
     * Captures a screenshot in the specified output type.
     *
     * @param outputType screenshot output type
     * @param <T> screenshot result type
     * @return screenshot in the specified output type
     */
    public static <T> T getScreenshotAs(OutputType<T> outputType) {
        WebDriver webDriver = getDriver();
        if (!(webDriver instanceof TakesScreenshot)) {
            throw new IllegalStateException("WebDriver does not support screenshots.");
        }
        try {
            return ((TakesScreenshot) webDriver).getScreenshotAs(outputType);
        } catch (Exception e) {
            throw new RuntimeException("Unable to capture screenshot.", e);
        }
    }
}
