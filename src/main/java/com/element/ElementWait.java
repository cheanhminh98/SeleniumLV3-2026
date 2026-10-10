package com.element;

import com.driver.DriverManager;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class ElementWait extends WebDriverWait {

    private final Element element;
    private final Duration timeout;

    private static final List<Class<? extends Throwable>> COMMON_RETRY_EXCEPTIONS = List.of(StaleElementReferenceException.class);

    /**
     * Creates an ElementWait using the timeout and polling interval
     * configured in DriverManager.
     *
     * @param element element to wait for
     */
    ElementWait(Element element) {
        this(element, DriverManager.getTimeout());
    }

    /**
     * Creates an ElementWait using the specified timeout.
     *
     * @param element element to wait for
     * @param timeout timeout
     */
    ElementWait(Element element, Duration timeout) {
        super(DriverManager.getDriver(), Objects.requireNonNull(timeout, "Timeout cannot be null."), DriverManager.getPollingInterval());
        this.element = Objects.requireNonNull(element, "Element cannot be null.");
        this.timeout = timeout;
        ignoreAll(COMMON_RETRY_EXCEPTIONS);
    }

    /**
     * Waits until the specified ElementCondition is satisfied.
     *
     * @param condition element condition
     */
    public void waitUntil(ElementCondition condition) {
        Objects.requireNonNull(condition, "ElementCondition cannot be null.");
        super.until(driver -> condition.matches(element));
    }

    /**
     * Creates a new wait using the same element and timeout.
     *
     * @param additionalExceptions additional exceptions to ignore
     * @return new ElementWait
     */
    ElementWait createWait(List<Class<? extends Throwable>> additionalExceptions) {
        Objects.requireNonNull(additionalExceptions, "Additional exceptions cannot be null.");
        ElementWait retryWait = new ElementWait(element, timeout);
        retryWait.ignoreAll(additionalExceptions);
        return retryWait;
    }

    /**
     * Creates the timeout exception with information about
     * the element that caused the timeout.
     *
     * @param message default timeout message
     * @param lastException last exception encountered during polling
     * @return timeout exception
     */
    @Override
    protected RuntimeException timeoutException(String message, Throwable lastException) {
        return new TimeoutException(
                "Element operation timed out after "
                        + timeout.toMillis()
                        + " milliseconds. "
                        + "Locator: " + element.getLocator(), lastException);
    }
}
