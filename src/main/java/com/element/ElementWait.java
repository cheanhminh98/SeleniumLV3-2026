package com.element;

import com.driver.DriverManager;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ElementWait extends WebDriverWait {

    private final Element element;

    /**
     * Creates an ElementWait using the timeout and polling interval
     * configured in DriverManager.
     *
     * @param element element to wait for
     */
    ElementWait(Element element) {
        super(DriverManager.getDriver(), DriverManager.getTimeout(), DriverManager.getPollingInterval());
        this.element = Objects.requireNonNull(element, "Element cannot be null.");
        ignoring(NoSuchElementException.class);
        ignoring(StaleElementReferenceException.class);
    }

    /**
     * Waits until the specified condition is satisfied
     * using additional exceptions to ignore.
     *
     * @param additionalExceptions additional exceptions to ignore
     * @param condition wait condition
     * @param <T> result type
     * @return condition result
     */
    public <T> T until(List<Class<? extends Throwable>> additionalExceptions, Function<WebDriver, T> condition) {
        Objects.requireNonNull(additionalExceptions, "Additional exceptions cannot be null.");
        Objects.requireNonNull(condition, "Wait condition cannot be null.");
        additionalExceptions.forEach(this::ignoring);
        return super.until(condition);
    }

    /**
     * Waits until the specified ElementCondition is satisfied.
     *
     * @param condition element condition
     */
    public void until(ElementCondition condition) {
        Objects.requireNonNull(condition, "ElementCondition cannot be null.");
        super.until(driver -> condition.matches(element));
    }
}