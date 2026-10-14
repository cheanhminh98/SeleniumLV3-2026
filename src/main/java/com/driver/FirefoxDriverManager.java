package com.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDriverManager extends BaseDriver<FirefoxOptions> {

    public String getName() {
        return "firefox";
    }

    /**
     * Creates a Firefox WebDriver.
     *
     * @param options FirefoxOptions
     * @return Firefox WebDriver
     */
    @Override
    protected WebDriver createDriver(FirefoxOptions options) {
        return new FirefoxDriver(options);
    }

    /**
     * Creates Firefox browser options.
     *
     * @param config Driver configuration
     * @return FirefoxOptions
     */
    @Override
    protected FirefoxOptions getOptions(DriverConfig config) {
        FirefoxOptions options = new FirefoxOptions();
        if (config.isHeadless()) {
            options.addArguments("-headless");
        }
        options.addArguments("-private");
        options.addPreference("dom.webnotifications.enabled", false);
        return options;
    }
}
