package com.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeDriverManager extends BaseDriver<EdgeOptions> {

    /**
     * Creates a Edge WebDriver.
     *
     * @param options EdgeOptions
     * @return Chrome WebDriver
     */
    @Override
    protected WebDriver createDriver(EdgeOptions options) {
        return new EdgeDriver(options);
    }


    /**
     * Creates Chrome options.
     *
     * @param config Driver Config
     * @return Chrome options
     */
    protected EdgeOptions getOptions(DriverConfig config) {
        EdgeOptions options = new EdgeOptions();
        if (config.isHeadless()) {
            options.addArguments("--headless=new");
        }
        if (config.getArguments() != null) {
            options.addArguments(config.getArguments());
        }
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-notifications");
        return options;
    }
}
