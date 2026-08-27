package com.driver;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public abstract class BaseDriver<T extends MutableCapabilities> {

    /**
     * Get browser options.
     *
     * @param config Driver Config
     * @return browser options
     */
    protected abstract T getOptions(DriverConfig config);

    /**
     * Creates a WebDriver.
     *
     * @param options browser options
     * @return WebDriver
     */
    protected abstract WebDriver createDriver(T options);

    /**

    /**
     * Creates a WebDriver using the specified options.
     *
     * @param config Driver Config
     * @return WebDriver
     */
    public WebDriver createWebDriver(DriverConfig config) {
        T options = getOptions(config);
        if (config.isRemote()) {
            return createRemoteDriver(config, options);
        }
        return createDriver(options);
    }

    /**
     * Creates a remote WebDriver.
     *
     * @param config Driver Config
     * @param options browser options
     * @return remote WebDriver
     */
    protected WebDriver createRemoteDriver(DriverConfig config, T options) {
        try {
            RemoteWebDriver driver = new RemoteWebDriver(new URL(config.getRemoteURL()), options);
            driver.setFileDetector(new LocalFileDetector());
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Cannot start browser when using remote: " + config.getRemoteURL() , e);
        }
    }
}
