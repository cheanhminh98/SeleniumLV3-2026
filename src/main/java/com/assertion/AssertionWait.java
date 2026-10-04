package com.assertion;

import com.driver.DriverManager;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AssertionWait extends WebDriverWait {

    private static final List<Class<? extends Throwable>> COMMON_RETRY_EXCEPTIONS =
            List.of(StaleElementReferenceException.class);

    public AssertionWait(Duration timeout) {
        super(DriverManager.getDriver(), timeout, DriverManager.getPollingInterval());
        ignoreAll(COMMON_RETRY_EXCEPTIONS);
    }

    @Override
    protected RuntimeException timeoutException(String message, Throwable lastException) {
        return new AssertionTimeoutException(message, lastException);
    }
}
