package com.driver;

import lombok.Getter;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.Objects;

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
        Objects.requireNonNull(config, "DriverConfig cannot be null.");
        this.config = config;
        this.driver = BaseDriverFactory
                .getDriver(config.getBrowser())
                .createWebDriver(config);
    }

    /**
     * Gets the default timeout.
     *
     * @return default timeout
     */
    public Duration getTimeout() {
        return config.getTimeout();
    }

    /**
     * Gets the polling interval.
     *
     * @return polling interval
     */
    public Duration getPollingInterval() {
        return config.getPollingInterval();
    }
}