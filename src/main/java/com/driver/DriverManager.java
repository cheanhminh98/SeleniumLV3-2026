package com.driver;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.Base64;

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
    public static void setDriver(WebDriver webDriver) {
        driver.set(webDriver);
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
                driver.remove();
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
     * Captures a screenshot from the current WebDriver.
     *
     * @return screenshot as byte array, or null if screenshot cannot be captured
     */
    public static byte[] captureScreenAsByte() {
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

    /**
     * Captures a screenshot as a Base64 string.
     *
     * @return screenshot encoded as Base64
     */
    public static String captureScreenAsBase64() {
        return Base64.getEncoder().encodeToString(captureScreenAsByte());
    }
}
