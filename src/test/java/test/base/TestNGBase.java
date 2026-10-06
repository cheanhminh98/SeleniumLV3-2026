package test.base;

import com.assertion.Assertion;
import com.data.BrowserType;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public abstract class TestNGBase extends TestBase implements IHookable {

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
     * Executes the test method and automatically finishes
     * all collected soft assertions.
     * Soft assertion failures are collected while the test
     * method is running. After the test method finishes,
     * {@link Assertion#finishTest()} evaluates all collected
     * failures and fails the test when necessary.
     *
     * @param callback TestNG test method callback
     * @param testResult current TestNG test result
     */
    @Override
    public void run(IHookCallBack callback, ITestResult testResult) {
        try {
            callback.runTestMethod(testResult);
            Assertion.finishTest();
        } finally {
            Assertion.clear();
        }
    }

    /**
     * Quits WebDriver.
     */
    @AfterClass(alwaysRun = true)
    public void afterClass() {
        tearDown();
    }
}
