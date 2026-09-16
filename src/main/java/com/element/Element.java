package com.element;

import com.driver.DriverManager;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class Element {

    private final By locator;

    @Getter
    private final ElementWait wait;

    private final ElementRetry retry;

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
        if (locator == null) {
            throw new IllegalArgumentException("Locator cannot be null.");
        }
        this.locator = locator;
        this.wait = new ElementWait(this);
        this.retry = new ElementRetry(wait);
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
        retry.retryAction(
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
        retry.retryAction(
                () -> getElement().sendKeys(value),
                INPUT_RETRY_EXCEPTIONS
        );
    }

    /**
     * Clicks the element.
     */
    public void click() {
        retry.retryAction(
                () -> getElement().click(),
                INTERACTION_RETRY_EXCEPTIONS
        );
    }

    /**
     * Checks the element if it is not already selected.
     */
    public void check() {
        retry.retryAction(
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
        retry.retryAction(
                () -> new Actions(DriverManager.getDriver())
                        .moveToElement(getElement())
                        .perform()
        );
    }

    /**
     * Scrolls the element into view.
     */
    public void scrollToView() {
        retry.retryAction(
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
        return retry.retryAction(
                () -> getElement().getText()
        );
    }

    /**
     * Gets the value attribute of the element.
     *
     * @return element value
     */
    public String getValue() {
        return retry.retryAction(
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
        wait.until(ElementConditions.isExist());
    }

    /**
     * Waits until the element is visible.
     *
     */
    public void waitForVisible() {
        wait.until(ElementConditions.isVisible());
    }

    /**
     * Waits until the element is clickable.
     *
     */
    public void waitForClickable() {
        wait.until(ElementConditions.isClickable());
    }

    /**
     * Waits until the element is enabled.
     *
     */
    public void waitForEnabled() {
        wait.until(ElementConditions.isEnabled());
    }

    /**
     * Waits until the element becomes invisible.
     *
     */
    public void waitForInvisible() {
        wait.until(ElementConditions.isInvisible());
    }

    /**
     * Waits until the element becomes disabled.
     *
     */
    public void waitForDisabled() {
        wait.until(ElementConditions.isDisabled());
    }

    /**
     * Waits until the specified condition is satisfied.
     *
     * @param condition condition to evaluate
     */
    public void waitUntil(ElementCondition condition) {
        wait.until(condition);
    }
}