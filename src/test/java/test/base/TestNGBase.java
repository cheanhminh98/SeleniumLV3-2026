package test.base;

import com.data.BrowserType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public abstract class TestNGBase extends TestBase {

    /**
     * Initializes WebDriver for the specified browser.
     *
     */
    @BeforeClass(alwaysRun = true)
    @Parameters("browser")
    public void beforeClass(@Optional("chrome") String browser) {
        setUp(BrowserType.getBrowser(browser));
    }

    /**
     * Quits WebDriver.
     */
    @AfterClass(alwaysRun = true)
    public void afterClass() {
        tearDown();
    }
}
