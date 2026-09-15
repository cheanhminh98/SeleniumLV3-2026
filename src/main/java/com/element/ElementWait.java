package com.element;

import com.driver.DriverManager;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class ElementWait {

    private final Element element;

    /**
     * Creates an ElementWait using the timeout and polling interval
     * configured in DriverManager.
     *
     * @param element element to wait for
     */
    ElementWait(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null.");
        }
        this.element = element;
    }

    /**
     * Waits until the specified Selenium condition is satisfied.
     *
     * @param condition Selenium wait condition
     * @param <T> result type
     * @return condition result
     */
    public <T> T until(Function<WebDriver, T> condition) {
        if (condition == null) {
            throw new IllegalArgumentException("Wait condition cannot be null.");
        }
        return createWait().until(condition);
    }

    /**
     * Waits until the specified Selenium condition is satisfied
     * using additional retry exceptions.
     *
     * <p>
     * The default retry exceptions are always ignored.
     * Additional exceptions are added for this operation.
     * </p>
     *
     * @param additionalExceptions additional exceptions to ignore
     * @param condition Selenium wait condition
     * @param <T> result type
     * @return condition result
     */
    public <T> T until(List<Class<? extends Throwable>> additionalExceptions, Function<WebDriver, T> condition) {
        if (additionalExceptions == null) {
            throw new IllegalArgumentException("Additional exceptions cannot be null.");
        }
        if (condition == null) {
            throw new IllegalArgumentException("Wait condition cannot be null.");
        }
        FluentWait<WebDriver> wait = createWait();
        additionalExceptions.forEach(wait::ignoring);
        return wait.until(condition);
    }

    /**
     * Waits until the specified ElementCondition is satisfied.
     *
     * @param condition element condition
     */
    public void until(ElementCondition condition) {
        if (condition == null) {
            throw new IllegalArgumentException("ElementCondition cannot be null.");
        }
        createWait().until(driver -> condition.matches(element));
    }

    /**
     * Creates a FluentWait using the timeout and polling interval
     * configured in DriverManager.
     *
     * @return configured FluentWait
     */
    private FluentWait<WebDriver> createWait() {
        return new FluentWait<>(DriverManager.getDriver())
                .withTimeout(DriverManager.getTimeout())
                .pollingEvery(DriverManager.getPollingInterval())
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }
}