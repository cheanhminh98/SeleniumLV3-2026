package test.base;

import com.assertion.Assertion;
import com.assertion.HardAssertion;
import com.assertion.SoftAssertion;
import com.data.BrowserType;
import com.driver.DriverConfig;
import com.driver.DriverManager;
import com.integration.TestNGAssertionIntegration;
import com.utilities.DriverConfigLoader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;

public abstract class TestBase implements TestNGAssertionIntegration {

    protected DriverConfigLoader driverConfigLoader = new DriverConfigLoader();
    protected final SoftAssertion softAssert = new SoftAssertion();
    protected final HardAssertion hardAssert = new HardAssertion();

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
