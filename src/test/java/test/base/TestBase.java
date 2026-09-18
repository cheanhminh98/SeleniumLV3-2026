package test.base;

import com.data.BrowserType;
import com.driver.BaseDriverFactory;
import com.driver.DriverConfig;
import com.driver.DriverManager;
import com.utilities.DriverConfigLoader;
import lombok.Getter;
import org.openqa.selenium.WebDriver;

public abstract class TestBase {

    protected DriverConfigLoader driverConfigLoader = new DriverConfigLoader();

    /**
     * Initializes WebDriver for the specified browser.
     *
     * @param browserType browser type
     */
    protected void setUp(BrowserType browserType) {
        DriverConfig driverConfig = driverConfigLoader.getDriverConfig(browserType);
        DriverManager.initialize(driverConfig);
        DriverManager.open(driverConfig.getBaseUrl());
    }

    /**
     * Quits WebDriver.
     */
    protected void tearDown() {
        DriverManager.quitDriver();
    }

    /**
     * Gets the current WebDriver.
     *
     * @return current WebDriver
     */
    public WebDriver getDriver() {
        WebDriver webDriver = DriverManager.getDriver();
        if (webDriver == null) {
            throw new IllegalStateException(
                    "WebDriver has not been initialized."
            );
        }
        return webDriver;
    }
}
