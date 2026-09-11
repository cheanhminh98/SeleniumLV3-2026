package com.driver;

import lombok.Getter;
import org.openqa.selenium.WebDriver;

@Getter
public class DriverContainer {

    private final DriverConfig config;
    private final WebDriver driver;

    /**
     * Creates a DriverContainer.
     *
     * @param config driver configuration
     */
    public DriverContainer(DriverConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("DriverConfig cannot be null.");
        }
        this.config = config;
        this.driver = BaseDriverFactory
                .getDriver(config.getBrowser())
                .createWebDriver(config);
    }


    /**
     * Gets the driver configuration.
     *
     * @return driver configuration
     */
    public DriverConfig getConfig() {
        return config;
    }

    /**
     * Gets the WebDriver.
     *
     * @return WebDriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }
}