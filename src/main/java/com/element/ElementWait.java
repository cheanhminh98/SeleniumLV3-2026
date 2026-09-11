package com.element;

import com.driver.DriverConfig;
import com.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.function.Function;

public class ElementWait {

    private final By locator;
    private final boolean alwaysFind;
    private WebElement element;

    /**
     * Creates an ElementWait.
     *
     * @param locator Selenium locator
     * @param alwaysFind whether to find the element every time
     */
    public ElementWait(By locator, boolean alwaysFind) {
        if (locator == null) {
            throw new IllegalArgumentException("Locator cannot be null.");
        }
        this.locator = locator;
        this.alwaysFind = alwaysFind;
    }

    /**
     * Gets a FluentWait using the default timeout.
     *
     * @return FluentWait instance
     */
    private FluentWait<WebDriver> getWait() {
        DriverConfig config = DriverManager.getConfig();
        return getWait(config.getTimeout());
    }

    /**
     * Gets a FluentWait using the specified timeout.
     *
     * @param timeout maximum time to wait
     * @return FluentWait instance
     */
    private FluentWait<WebDriver> getWait(Duration timeout) {
        if (timeout == null) {
            throw new IllegalArgumentException("Timeout cannot be null.");
        }

        if (timeout.isNegative()) {
            throw new IllegalArgumentException("Timeout cannot be negative.");
        }
        DriverConfig config = DriverManager.getConfig();
        return new FluentWait<>(DriverManager.getDriver())
                .withTimeout(timeout)
                .pollingEvery(config.getPollingInterval())
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    /**
     * Waits for the specified condition using the default timeout.
     *
     * @param condition wait condition
     * @param <T> result type
     * @return condition result
     */
    private <T> T waitUntil(Function<WebDriver, T> condition) {
        return getWait().until(condition);
    }

    /**
     * Waits for the specified condition using the given timeout.
     *
     * @param timeout maximum time to wait
     * @param condition wait condition
     * @param <T> result type
     * @return condition result
     */
    private <T> T waitUntil(Duration timeout, Function<WebDriver, T> condition) {
        return getWait(timeout).until(condition);
    }

    /**
     * Gets the current element.
     *
     * @return current web element
     */
    private WebElement getElement() {
        if (alwaysFind || element == null) {
            element = findElement();
            return element;
        }
        try {
            element.isEnabled();
            return element;
        } catch (StaleElementReferenceException e) {
            element = findElement();
            return element;
        }
    }

    /**
     * Finds the element using the locator.
     *
     * @return web element
     */
    private WebElement findElement() {
        return DriverManager.getDriver().findElement(locator);
    }

    /**
     * Waits until the element exists.
     *
     * @return existing web element
     */
    public WebElement waitForExist() {
        return waitUntil(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits until the element exists.
     *
     * @param timeout maximum time to wait
     * @return existing web element
     */
    public WebElement waitForExist(Duration timeout) {
        return waitUntil(timeout, ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits until the element is visible.
     *
     * @return visible web element
     */
    public WebElement waitForVisible() {
        return waitUntil(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the element is visible.
     *
     * @param timeout maximum time to wait
     * @return visible web element
     */
    public WebElement waitForVisible(Duration timeout) {
        return waitUntil(timeout, ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the element is clickable.
     *
     * @return clickable web element
     */
    public WebElement waitForClickable() {
        return waitUntil(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits until the element is clickable.
     *
     * @param timeout maximum time to wait
     * @return clickable web element
     */
    public WebElement waitForClickable(Duration timeout) {
        return waitUntil(timeout, ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits until the element is enabled.
     *
     * @return enabled web element
     */
    public WebElement waitForEnabled() {
        return waitUntil(driver -> {
            WebElement webElement = getElement();
            return webElement.isEnabled() ? webElement : null;
        });
    }

    /**
     * Waits until the element is enabled.
     *
     * @param timeout maximum time to wait
     * @return enabled web element
     */
    public WebElement waitForEnabled(Duration timeout) {
        return waitUntil(timeout, driver -> {
            WebElement webElement = getElement();
                    return webElement.isEnabled() ? webElement : null;
        });
    }

    /**
     * Waits until the element becomes invisible.
     *
     * @return true if the element becomes invisible
     */
    public boolean waitForInvisible() {
        return waitUntil(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits until the element becomes invisible.
     *
     * @param timeout maximum time to wait
     * @return true if the element becomes invisible
     */
    public boolean waitForInvisible(Duration timeout) {
        return waitUntil(timeout, ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits until the element becomes disabled.
     *
     * @return true if the element becomes disabled
     */
    public boolean waitForDisabled() {
        return waitUntil(driver -> !getElement().isEnabled());
    }

    /**
     * Waits until the element becomes disabled.
     *
     * @param timeout maximum time to wait
     * @return true if the element becomes disabled
     */
    public boolean waitForDisabled(Duration timeout) {
        return waitUntil(timeout, driver -> !getElement().isEnabled()
        );
    }
}