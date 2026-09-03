package test.base;

import com.data.BrowserType;
import com.integration.JUnitReportExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(JUnitReportExtension.class)
public abstract class JUnitBase extends TestBase {

    /**
     * Initializes WebDriver before each test.
     */
    @BeforeEach
    public void beforeEach() {
        String browser = System.getProperty("browser");
        setUp(BrowserType.getBrowser(browser));
    }

    /**
     * Quits WebDriver after each test.
     */
    @AfterEach
    public void afterEach() {
        tearDown();
    }
}
