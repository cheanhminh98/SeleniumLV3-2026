package com.element;

import com.driver.DriverManager;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Objects;

public class ElementWait extends WebDriverWait {

    private final Element element;

    private static final List<Class<? extends Throwable>> COMMON_RETRY_EXCEPTIONS = List.of(
            StaleElementReferenceException.class
    );

    /**
     * Creates an ElementWait using the timeout and polling interval
     * configured in DriverManager.
     *
     * @param element element to wait for
     */
    ElementWait(Element element) {
        super(DriverManager.getDriver(), DriverManager.getTimeout(), DriverManager.getPollingInterval());
        this.element = Objects.requireNonNull(element, "Element cannot be null.");
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

    ElementWait createWait(List<Class<? extends Throwable>> additionalExceptions) {
        Objects.requireNonNull(additionalExceptions, "Additional exceptions cannot be null.");
        ElementWait retryWait = new ElementWait(element);
        retryWait.ignoreAll(additionalExceptions);
        return retryWait;
    }
}
