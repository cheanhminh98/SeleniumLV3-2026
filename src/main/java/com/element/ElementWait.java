package com.element;

import com.driver.DriverConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.Objects;
import java.util.function.Function;

/**
 * Provides explicit wait functionality for a web element.
 */
public class ElementWait {

    private final WebDriver driver;
    private final By locator;
    private final DriverConfig driverConfig;
    private final boolean alwaysFind;

    private WebElement element;

    /**
     * Creates an element wait handler.
     *
     * @param driver       WebDriver instance
     * @param locator      element locator
     * @param driverConfig driver configuration
     * @param alwaysFind   whether the element should be searched before each operation
     * @param element      cached web element
     */
    public ElementWait(
            WebDriver driver,
            By locator,
            DriverConfig driverConfig,
            boolean alwaysFind,
            WebElement element
    ) {
        this.driver = Objects.requireNonNull(
                driver,
                "WebDriver cannot be null."
        );

        this.locator = Objects.requireNonNull(
                locator,
                "Locator cannot be null."
        );

        this.driverConfig = Objects.requireNonNull(
                driverConfig,
                "DriverConfig cannot be null."
        );

        this.alwaysFind = alwaysFind;
        this.element = element;
    }

    /**
     * Creates the configured FluentWait instance.
     *
     * @return configured FluentWait
     */
    private FluentWait<WebDriver> getWait() {
        return new FluentWait<>(driver)
                .withTimeout(driverConfig.getTimeout())
                .pollingEvery(driverConfig.getPollingInterval())
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    /**
     * Gets the current target element.
     *
     * @return target web element
     */
    private WebElement getTargetElement() {
        if (alwaysFind || element == null) {
            element = driver.findElement(locator);
        }

        return element;
    }

    /**
     * Executes a wait condition with a custom timeout message.
     *
     * @param message   timeout message
     * @param condition wait condition
     * @param <T>       condition result type
     * @return condition result
     */
    private <T> T waitUntil(
            String message,
            Function<WebDriver, T> condition
    ) {
        return getWait()
                .withMessage(message)
                .until(condition);
    }

    /**
     * Builds a timeout message.
     *
     * @param condition condition description
     * @return timeout message
     */
    private String timeoutMessage(String condition) {
        return "Element was not " + condition
                + " within " + driverConfig.getTimeout()
                + ": " + locator;
    }

    /**
     * Waits until the element is visible.
     *
     * @return visible web element
     */
    public WebElement waitForVisible() {
        Function<WebDriver, WebElement> condition =
                alwaysFind || element == null
                        ? ExpectedConditions.visibilityOfElementLocated(locator)
                        : ExpectedConditions.visibilityOf(element);

        return element = waitUntil(
                timeoutMessage("visible"),
                condition
        );
    }

    /**
     * Waits until the element is clickable.
     *
     * @return clickable web element
     */
    public WebElement waitForClickable() {
        Function<WebDriver, WebElement> condition =
                alwaysFind || element == null
                        ? ExpectedConditions.elementToBeClickable(locator)
                        : ExpectedConditions.elementToBeClickable(element);

        return element = waitUntil(
                timeoutMessage("clickable"),
                condition
        );
    }

    /**
     * Waits until the element is enabled.
     *
     * @return enabled web element
     */
    public WebElement waitForEnabled() {
        return element = waitUntil(
                timeoutMessage("enabled"),
                driver -> {
                    WebElement target = getTargetElement();
                    return target.isEnabled() ? target : null;
                }
        );
    }

    /**
     * Waits until the element is invisible or no longer exists.
     *
     * @return true when the element is invisible or absent
     */
    public boolean waitForInvisible() {
        Function<WebDriver, Boolean> condition =
                alwaysFind || element == null
                        ? ExpectedConditions.invisibilityOfElementLocated(locator)
                        : ExpectedConditions.invisibilityOf(element);

        return waitUntil(
                "Element was still visible after "
                        + driverConfig.getTimeout()
                        + ": " + locator,
                condition
        );
    }

    /**
     * Waits until the element is disabled.
     *
     * @return disabled web element
     */
    public WebElement waitForDisabled() {
        return element = waitUntil(
                timeoutMessage("disabled"),
                driver -> {
                    WebElement target = getTargetElement();
                    return target.isEnabled() ? null : target;
                }
        );
    }
}