package com.element;

import com.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

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
     * Creates an Element from a Selenium By locator.
     *
     * @param locator Selenium locator
     */
    public Element(By locator) {
        Objects.requireNonNull(locator, "Locator cannot be null.");
        this.locator = locator;
    }

    /**
     * Creates a new ElementWait using the current WebDriver.
     *
     * @return ElementWait for the current driver
     */
    private ElementWait getWait() {
        return new ElementWait(this);
    }

    /**
     * Creates a new ElementRetry using the current ElementWait.
     *
     * @return ElementRetry for the current driver
     */
    private ElementRetry getRetry() {
        return new ElementRetry(getWait());
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
     * Gets the visible text of the element.
     *
     * @return element text
     */
    public String getText() {
        return getRetry().retryValue(
                () -> getElement().getText()
        );
    }

    /**
     * Gets the value attribute of the element.
     *
     * @return element value
     */
    public String getValue() {
        return getRetry().retryValue(
                () -> getElement().getAttribute("value")
        );
    }

    /**
     * Checks whether the element is displayed.
     *
     * @return true if the element exists and is displayed
     */
    public boolean isDisplayed() {
        List<WebElement> elements =
                DriverManager.getDriver()
                        .findElements(locator);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    /**
     * Checks whether the element exists.
     *
     * @return true if at least one matching element exists
     */
    public boolean isExist() {
        return !DriverManager.getDriver()
                .findElements(locator)
                .isEmpty();
    }

    /**
     * Waits until the element exists.
     *
     */
    public void waitForExist() {
        getWait().waitUntil(ElementConditions.isExist());
    }

    /**
     * Waits until the element is visible.
     *
     */
    public void waitForVisible() {
        getWait().waitUntil(ElementConditions.isVisible());
    }

    /**
     * Waits until the element is clickable.
     *
     */
    public void waitForClickable() {
        getWait().waitUntil(ElementConditions.isClickable());
    }

    /**
     * Waits until the element is enabled.
     *
     */
    public void waitForEnabled() {
        getWait().waitUntil(ElementConditions.isEnabled());
    }

    /**
     * Waits until the element becomes invisible.
     *
     */
    public void waitForInvisible() {
        getWait().waitUntil(ElementConditions.isInvisible());
    }

    /**
     * Waits until the element becomes disabled.
     *
     */
    public void waitForDisabled() {
        getWait().waitUntil(ElementConditions.isDisabled());
    }

    /**
     * Waits until the specified condition is satisfied.
     *
     * @param condition condition to evaluate
     */
    public void waitUntil(ElementCondition condition) {
        getWait().waitUntil(condition);
    }
}
