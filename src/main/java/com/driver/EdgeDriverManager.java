package com.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeDriverManager extends BaseDriver<EdgeOptions> {

    public String getName() {
        return "edge";
    }

    /**
     * Creates an Edge WebDriver.
     *
     * @param options EdgeOptions
     * @return Edge WebDriver
     */
    @Override
    protected WebDriver createDriver(EdgeOptions options) {
        return new EdgeDriver(options);
    }

    /**
     * Creates Edge browser options.
     *
     * @param config Driver configuration
     * @return EdgeOptions
     */
    @Override
    protected EdgeOptions getOptions(DriverConfig config) {
        EdgeOptions options = new EdgeOptions();
        if (config.isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--inprivate");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-notifications");
        return options;
    }
}
