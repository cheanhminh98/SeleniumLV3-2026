package com.element;

import com.driver.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class Element {

    private final By locator;

    private static final List<Class<? extends Throwable>>
            INTERACTION_RETRY_EXCEPTIONS = List.of(
            ElementClickInterceptedException.class,
            ElementNotInteractableException.class
    );

    private static final List<Class<? extends Throwable>>
            INPUT_RETRY_EXCEPTIONS = List.of(
            ElementNotInteractableException.class
    );

    /**
     * Creates an element using the specified locator.
     *
     * @param locator element locator
     */
    public Element(By locator) {
        this.locator = Objects.requireNonNull(locator, "Locator cannot be null.");
    }

    /**
     * Gets the locator used by this element.
     *
     * @return element locator
     */
    By getLocator() {
        return locator;
    }

    /**
     * Gets the first matching WebElement.
     *
     * @return matching WebElement
     */
    public WebElement getElement() {
        return DriverManager.getDriver().findElement(locator);
    }

    /**
     * Gets all matching WebElements.
     *
     * @return matching WebElements
     */
    public List<WebElement> getElements() {
        return DriverManager.getDriver().findElements(locator);
    }

    /**
     * Creates an ElementWait using the timeout from driver configuration.
     *
     * @return ElementWait
     */
    private ElementWait getWait() {
        return new ElementWait(this, DriverManager.getTimeout());
    }

    /**
     * Creates an ElementRetry using the default element timeout.
     *
     * @return ElementRetry
     */
    private ElementRetry getRetry() {
        Duration timeout = DriverManager.getTimeout();
        return new ElementRetry(new ElementWait(this, timeout), timeout);
    }

    /**
     * Creates an ElementRetry using the specified timeout.
     *
     * @param timeout timeout
     * @return ElementRetry
     */
    private ElementRetry getRetry(Duration timeout) {
        return new ElementRetry(new ElementWait(this, timeout), timeout);
    }

    /**
     * Sets the value of the element.
     *
     * @param value value to enter
     */
    public void setValue(String value) {
        getRetry().retryAction(
                () -> {
                    WebElement webElement = getElement();
                    webElement.clear();
                    webElement.sendKeys(value);
                },
                INPUT_RETRY_EXCEPTIONS
        );
    }

    /**
     * Sends a value to the element without clearing it first.
     *
     * @param value value to enter
     */
    public void enter(String value) {
        getRetry().retryAction(
                () -> getElement().sendKeys(value),
                INPUT_RETRY_EXCEPTIONS
        );
    }

    /**
     * Clicks the element.
     */
    public void click() {
        getRetry().retryAction(
                () -> getElement().click(),
                INTERACTION_RETRY_EXCEPTIONS
        );
    }

    /**
     * Checks the element if it is not already selected.
     */
    public void check() {
        getRetry().retryAction(
                () -> {
                    WebElement webElement = getElement();
                    if (!webElement.isSelected()) {
                        webElement.click();
                    }
                },
                INTERACTION_RETRY_EXCEPTIONS
        );
    }

    /**
     * Moves the mouse over the element.
     */
    public void hover() {
        getRetry().retryAction(
                () -> new Actions(DriverManager.getDriver())
                        .moveToElement(getElement())
                        .perform()
        );
    }

    /**
     * Scrolls the element into view.
     */
    public void scrollToView() {
        getRetry().retryAction(
                () -> {
                    WebElement webElement = getElement();
                    ((JavascriptExecutor) DriverManager.getDriver())
                            .executeScript(
                                    "arguments[0].scrollIntoView({block: 'center'});",
                                    webElement
                            );
                }
        );
    }

    /**
     * Gets the visible text of the element without retry.
     *
     * @return element text
     */
    public String getText() {
        return getText(DriverManager.getTimeout());
    }

    /**
     * Gets the visible text of the element.
     *
     * @param timeout timeout
     * @return element text
     */
    public String getText(Duration timeout) {
        return getRetry(timeout)
                .retryValue(() -> getElement().getText());
    }

    /**
     * Gets the value attribute of the element without retry.
     *
     * @return element value
     */
    public String getValue() {
        return getValue(DriverManager.getTimeout());
    }

    /**
     * Gets the value attribute of the element.
     *
     * @param timeout timeout
     * @return element value
     */
    public String getValue(Duration timeout) {
        return getRetry(timeout)
                .retryValue(() -> getElement().getAttribute("value"));
    }

    /**
     * Checks whether the element is displayed without retry.
     *
     * @return true if displayed; otherwise false
     */
    public boolean isDisplayed() {
        return isDisplayed(DriverManager.getTimeout());
    }

    /**
     * Checks whether the element is displayed.
     *
     * @param timeout timeout
     */
    public boolean isDisplayed(Duration timeout) {
        return getRetry(timeout).retryValue(() -> getElement().isDisplayed());

    }

    /**
     * Checks whether the element exists.
     *
     * @return true if the element exists; otherwise false
     */
    public boolean isExist() {
        return !DriverManager.getDriver()
                .findElements(locator)
                .isEmpty();
    }

    /**
     * Checks whether the element is checked without retry.
     *
     * @return true if checked; otherwise false
     */
    public boolean isChecked() {
        return isChecked(DriverManager.getTimeout());
    }

    /**
     * Checks whether the element is checked.
     *
     * @param timeout timeout
     */
    public boolean isChecked(Duration timeout) {
        return getRetry(timeout).retryValue(() -> getElement().isSelected());

    }

    /**
     * Checks whether the element is enabled without retry.
     *
     * @return true if enabled; otherwise false
     */
    public boolean isEnabled() {
        return isEnabled(DriverManager.getTimeout());
    }

    /**
     * Checks whether the element is enabled.
     *
     * @param timeout timeout
     */
    public boolean isEnabled(Duration timeout) {
        return getRetry(timeout).retryValue(() -> getElement().isEnabled());

    }

    /**
     * Waits until the element exists.
     */
    public void waitForExist() {
        getWait().waitUntil(ElementConditions.isExist());
    }

    /**
     * Waits until the element is visible.
     */
    public void waitForVisible() {
        getWait().waitUntil(ElementConditions.isVisible());
    }

    /**
     * Waits until the element is clickable.
     */
    public void waitForClickable() {
        getWait().waitUntil(ElementConditions.isClickable());
    }

    /**
     * Waits until the element is enabled.
     */
    public void waitForEnabled() {
        getWait().waitUntil(ElementConditions.isEnabled());
    }

    /**
     * Waits until the element is invisible.
     */
    public void waitForInvisible() {
        getWait().waitUntil(ElementConditions.isInvisible());
    }

    /**
     * Waits until the element is disabled.
     */
    public void waitForDisabled() {
        getWait().waitUntil(ElementConditions.isDisabled());
    }

    /**
     * Waits until the specified condition is satisfied.
     *
     * @param condition element condition
     */
    public void waitUntil(ElementCondition condition) {
        Objects.requireNonNull(condition, "ElementCondition cannot be null.");

        getWait().waitUntil(condition);
    }
}
