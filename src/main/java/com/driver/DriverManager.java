package com.driver;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Gets the current WebDriver.
     *
     * @return current WebDriver
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Sets the WebDriver.
     *
     * @param webDriver WebDriver instance
     */
    public void setDriver(WebDriver webDriver) {
        driver.set(webDriver);
    }

    /**
     * Quits the WebDriver.
     */
    public void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    /**
     * Opens the URL.
     *
     * @param url to open
     */
    public void open(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL cannot be null or empty.");
        }
        getDriver().navigate().to(url);
    }

    /**
     * Captures a screenshot from the current WebDriver.
     *
     * @return screenshot as byte array, or null if screenshot cannot be captured
     */
    public byte[] captureScreen() {
        WebDriver webDriver = getDriver();
        if (webDriver == null) {
            throw new IllegalStateException("WebDriver has not been initialized.");
        }
        if (!(webDriver instanceof TakesScreenshot)) {throw new IllegalStateException(
                "WebDriver does not support screenshots.");
        }
        try {
            return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            throw new RuntimeException("Unable to capture screenshot.", e);
        }
    }
}
