package com.element;

import com.driver.DriverConfig;
import com.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Objects;

/**
 * Represents a web element and provides common actions,
 * state checks, and automatic wait functionality.
 */
public class Element {

    private final DriverConfig driverConfig;
    private final By locator;
    private final boolean alwaysFind;

    private WebElement element;

    /**
     * Creates an element using an XPath locator.
     *
     * <p>The element uses automatic waiting with the timeout
     * configured in {@link DriverConfig}.</p>
     *
     * @param locator      XPath locator
     * @param driverConfig driver configuration
     */
    public Element(String locator, DriverConfig driverConfig) {
        this(locator, driverConfig, true);
    }

    /**
     * Creates an element using an XPath locator.
     *
     * @param locator      XPath locator
     * @param driverConfig driver configuration
     * @param alwaysFind   whether the element should be searched before each operation
     */
    public Element(String locator, DriverConfig driverConfig, boolean alwaysFind) {
        this(By.xpath(locator), driverConfig, alwaysFind);
    }

    /**
     * Creates an element using a Selenium locator.
     *
     * <p>The element uses automatic waiting with the timeout
     * configured in {@link DriverConfig}.</p>
     *
     * @param locator      Selenium locator
     * @param driverConfig driver configuration
     */
    public Element(By locator, DriverConfig driverConfig) {
        this(locator, driverConfig, true);
    }

    /**
     * Creates an element using a Selenium locator.
     *
     * @param locator      Selenium locator
     * @param driverConfig driver configuration
     * @param alwaysFind   whether the element should be searched before each operation
     */
    public Element(By locator, DriverConfig driverConfig, boolean alwaysFind) {
        this.locator = Objects.requireNonNull(locator, "Locator cannot be null.");
        this.driverConfig = Objects.requireNonNull(driverConfig, "DriverConfig cannot be null.");
        this.alwaysFind = alwaysFind;
    }

    /**
     * Gets the current WebDriver.
     *
     * @return current WebDriver
     */
    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    /**
     * Gets the element wait handler.
     *
     * @return element wait handler
     */
    protected ElementWait getWait() {
        return new ElementWait(getDriver(), locator, driverConfig, alwaysFind, element);
    }

    /**
     * Gets the web element using automatic waiting.
     *
     * <p>When {@code alwaysFind} is true, the element is searched again
     * before every operation. When false, the element is cached after
     * the first successful lookup.</p>
     *
     * <p>If the element does not become visible within the configured
     * timeout, {@link org.openqa.selenium.TimeoutException} is thrown.</p>
     *
     * @return visible web element
     */
    protected WebElement getElement() {
        if (alwaysFind || element == null) {
            element = getWait().waitForVisible();
        }
        return element;
    }

    /**
     * Sets a value into the element.
     *
     * <p>The framework automatically waits for the element to become
     * visible before entering the value.</p>
     *
     * @param values values to enter
     */
    public void setValue(Object... values) {
        WebElement target = getElement();
        target.clear();
        for (Object value : values) {
            target.sendKeys(String.valueOf(value));
        }
    }

    /**
     * Gets the visible text of the element.
     *
     * <p>The framework automatically waits for the element to become
     * visible before retrieving the text.</p>
     *
     * @return element text
     */
    public String getText() {
        return getElement().getText();
    }

    /**
     * Gets an attribute value from the element.
     *
     * @param attributeName attribute name
     * @return attribute value
     */
    public String getAttribute(String attributeName) {
        Objects.requireNonNull(attributeName, "Attribute name cannot be null.");
        return getElement().getAttribute(attributeName);
    }

    /**
     * Gets the value attribute of the element.
     *
     * @return value attribute
     */
    public String getValue() {
        return getAttribute("value");
    }

    /**
     * Gets a CSS property value from the element.
     *
     * @param propertyName CSS property name
     * @return CSS property value
     */
    public String getCssValue(String propertyName) {
        Objects.requireNonNull(propertyName, "CSS property name cannot be null.");
        return getElement().getCssValue(propertyName);
    }

    /**
     * Clicks the element.
     *
     * <p>The framework automatically waits for the element to become
     * visible before clicking.</p>
     */
    public void click() {
        getElement().click();
    }

    /**
     * Checks the element if it is not already checked.
     */
    public void check() {
        if (!isChecked()) {
            click();
        }
    }

    /**
     * Enters a value into the element without clearing its existing value.
     *
     * @param value value to enter
     */
    public void enter(String value) {
        Objects.requireNonNull(value, "Value cannot be null.");
        getElement().sendKeys(value);
    }

    /**
     * Moves the mouse pointer to the element.
     */
    public void hover() {
        new Actions(getDriver()).moveToElement(getElement()).perform();
    }

    /**
     * Scrolls the element into the visible area of the browser.
     */
    public void scrollToView() {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) getDriver();
        javascriptExecutor.executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                getElement());
    }

    /**
     * Checks whether the element is selected.
     *
     * @return true if the element is selected; otherwise false
     */
    public boolean isChecked() {
        try {
            return getElement().isSelected();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Checks whether the element is displayed.
     *
     * @return true if the element is displayed; otherwise false
     */
    public boolean isDisplayed() {
        try {
            return getElement().isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Checks whether the element exists in the DOM.
     *
     * <p>This method does not wait for the configured timeout.
     * It performs an immediate existence check.</p>
     *
     * @return true if the element exists; otherwise false
     */
    public boolean isExist() {
        try {
            return !getDriver().findElements(locator).isEmpty();
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Waits until the element is visible.
     *
     * <p>The configured timeout from {@link DriverConfig} is used.</p>
     *
     * @throws org.openqa.selenium.TimeoutException if the timeout is exceeded
     */
    public void waitForVisible() {
        element = getWait().waitForVisible();
    }

    /**
     * Waits until the element is clickable.
     *
     * <p>The configured timeout from {@link DriverConfig} is used.</p>
     *
     * @throws org.openqa.selenium.TimeoutException if the timeout is exceeded
     */
    public void waitForClickable() {
        element = getWait().waitForClickable();
    }

    /**
     * Waits until the element is enabled.
     *
     * <p>The configured timeout from {@link DriverConfig} is used.</p>
     *
     * @throws org.openqa.selenium.TimeoutException if the timeout is exceeded
     */
    public void waitForEnabled() {
        element = getWait().waitForEnabled();
    }

    /**
     * Waits until the element is invisible.
     *
     * <p>The configured timeout from {@link DriverConfig} is used.</p>
     *
     * @throws org.openqa.selenium.TimeoutException if the timeout is exceeded
     */
    public void waitForInvisible() {
        getWait().waitForInvisible();
    }

    /**
     * Waits until the element is disabled.
     *
     * <p>The configured timeout from {@link DriverConfig} is used.</p>
     *
     * @throws org.openqa.selenium.TimeoutException if the timeout is exceeded
     */
    public void waitForDisabled() {
        element = getWait().waitForDisabled();
    }
}