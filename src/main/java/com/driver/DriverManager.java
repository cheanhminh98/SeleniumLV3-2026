package com.driver;

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
}
