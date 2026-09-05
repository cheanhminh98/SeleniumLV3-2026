package test.base;

import com.data.BrowserType;
import com.driver.BaseDriverFactory;
import com.driver.DriverConfig;
import com.driver.DriverManager;
import com.utilities.DriverConfigLoader;
import lombok.Getter;
import org.openqa.selenium.WebDriver;

public abstract class TestBase {

    protected DriverManager driverManager;
    protected DriverConfigLoader driverConfigLoader = new DriverConfigLoader();

    /**
     * Initializes WebDriver for the specified browser.
     *
     * @param browserType browser type
     */
    protected void setUp(BrowserType browserType) {
        DriverConfig driverConfig = driverConfigLoader.getDriverConfig(browserType);
        WebDriver webDriver = BaseDriverFactory
                .getDriver(browserType)
                .createWebDriver(driverConfig);
        driverManager = new DriverManager();
        driverManager.setDriver(webDriver);
        driverManager.open(driverConfig.getBaseUrl());
    }

    /**
     * Quits WebDriver.
     */
    protected void tearDown() {
        if (driverManager != null) {
            driverManager.quitDriver();
            driverManager = null;
        }
    }

    /**
     * Gets the current WebDriver.
     *
     * @return current WebDriver
     */
    public WebDriver getDriver() {
        WebDriver webDriver = DriverManager.getDriver();
        if (webDriver == null) {
            throw new IllegalStateException("WebDriver has not been initialized.");
        }
        return webDriver;
    }
}
