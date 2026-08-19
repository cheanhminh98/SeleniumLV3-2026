package com.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverProvider extends BaseDriver<ChromeOptions> {

    /**
     * Creates a Chrome WebDriver.
     *
     * @param options ChromeOptions
     * @return Chrome WebDriver
     */
    @Override
    protected WebDriver createDriver(ChromeOptions options) {
        return new ChromeDriver(options);
    }


    /**
     * Creates Chrome options.
     *
     * @param config Driver Config
     * @return Chrome options
     */
    protected ChromeOptions getOptions(DriverConfig config) {
        ChromeOptions options = new ChromeOptions();
        if (config.isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--incognito");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-notifications");
        return options;
    }
}
