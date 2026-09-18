package com.element;

import com.driver.DriverManager;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Objects;

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
     * Waits until the specified ElementCondition is satisfied.
     *
     * @param condition element condition
     */
    public void until(ElementCondition condition) {
        Objects.requireNonNull(condition, "ElementCondition cannot be null.");
        super.until(driver -> condition.matches(element));
    }
}