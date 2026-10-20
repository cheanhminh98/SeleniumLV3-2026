package com.driver;


import java.util.Locale;
import java.util.ServiceLoader;

public class BaseDriverFactory {

    /**
     * Gets the driver manager for the specified browser.
     *
     * @param browserType browser type
     * @return driver
     */
    public static BaseDriver<?> getDriver(String browserType) {
        if (browserType == null || browserType.isBlank()) {
            throw new IllegalArgumentException("Browser cannot be null or empty.");
        }
        String browserName = browserType.trim().toLowerCase(Locale.ROOT);

        return ServiceLoader.load(BaseDriver.class)
                .stream().map(ServiceLoader.Provider::get)
                .filter(driver -> driver.getName() != null
                        && driver.getName().trim()
                        .equalsIgnoreCase(browserName))
                .findFirst().orElseThrow(
                        () -> new IllegalArgumentException("Unsupported browser: " + browserName));
    }
}
