package test.base;

import com.assertion.HardAssertion;
import com.assertion.SoftAssertion;
import com.driver.DriverConfig;
import com.driver.DriverManager;
import com.utilities.DriverConfigLoader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;

public abstract class TestBase {

    protected final DriverConfigLoader driverConfigLoader =
            new DriverConfigLoader();

    protected final SoftAssertion softAssert = new SoftAssertion();
    protected final HardAssertion hardAssert = new HardAssertion();

    /**
     * Initializes WebDriver for the specified browser.
     *
     * @param browserName the browser provider name
     */
    protected void setUp(String browserName) {
        DriverConfig driverConfig = driverConfigLoader.getDriverConfig(browserName);
        DriverManager.initialize(driverConfig);
        DriverManager.open(driverConfig.getBaseUrl());
    }

    /**
     * Quits WebDriver.
     */
    @AfterClass(alwaysRun = true)
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
