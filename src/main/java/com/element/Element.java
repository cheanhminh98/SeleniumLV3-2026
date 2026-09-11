package com.element;

import com.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

public class Element {

    private final String locator;
    private final boolean alwaysFind;
    private WebElement element;

    /**
     * Creates an Element using the specified locator.
     *
     * @param locator element locator
     */
    public Element(String locator) {
        this(locator, false);
    }

    /**
     * Creates an Element using the specified locator.
     *
     * @param locator element locator
     * @param alwaysFind whether to find the element every time
     */
    public Element(String locator, boolean alwaysFind) {
        if (locator == null || locator.trim().isEmpty()) {
            throw new IllegalArgumentException("Locator cannot be null or empty.");
        }
        this.locator = locator;
        this.alwaysFind = alwaysFind;
    }

    /**
     * Creates an Element using the specified Selenium locator.
     *
     * @param locator Selenium By locator
     */
    public Element(By locator) {
        this(locator, false);
    }

    /**
     * Creates an Element using the specified Selenium locator.
     *
     * @param locator Selenium By locator
     * @param alwaysFind whether to find the element every time
     */
    public Element(By locator, boolean alwaysFind) {
        if (locator == null) {
            throw new IllegalArgumentException("Locator cannot be null.");
        }
        this.locator = locator.toString();
        this.alwaysFind = alwaysFind;
    }

    /**
     * Gets the web element.
     *
     * @return web element
     */
    public WebElement getElement() {
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
     * Finds the element using the configured locator.
     *
     * @return web element
     */
    private WebElement findElement() {
        return DriverManager.getDriver().findElement(getByLocator());
    }

    /**
     * Gets all elements matching the locator.
     *
     * @return list of web elements
     */
    public List<WebElement> getElements() {
        return DriverManager.getDriver().findElements(getByLocator());
    }

    /**
     * Sets the specified value to the element.
     *
     * @param value value to enter
     */
    public void setValue(String value) {
        WebElement webElement = getElement();
        webElement.clear();
        webElement.sendKeys(value);
    }

    /**
     * Enters the specified value without clearing
     * the existing value.
     *
     * @param value value to enter
     */
    public void enter(String value) {
        getElement().sendKeys(value);
    }

    /**
     * Clicks the element.
     */
    public void click() {
        getElement().click();
    }

    /**
     * Selects the element if it is not already selected.
     */
    public void check() {
        WebElement webElement = getElement();
        if (!webElement.isSelected()) {
            webElement.click();
        }
    }

    /**
     * Moves the mouse over the element.
     */
    public void hover() {
        new Actions(DriverManager.getDriver()).moveToElement(getElement()).perform();
    }

    /**
     * Scrolls the element into view.
     */
    public void scrollToView() {
        WebElement webElement = getElement();
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});",
                        webElement
        );
    }

    /**
     * Gets the visible text of the element.
     *
     * @return element text
     */
    public String getText() {
        return getElement().getText();
    }

    /**
     * Gets the value attribute of the element.
     *
     * @return element value
     */
    public String getValue() {
        return getElement().getAttribute("value");
    }

    /**
     * Checks whether the element is displayed.
     *
     * @return true if displayed
     */
    public boolean isDisplayed() {
        List<WebElement> elements = DriverManager.getDriver().findElements(getByLocator());
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    /**
     * Checks whether the element exists.
     *
     * @return true if element exists
     */
    public boolean isExist() {
        return !DriverManager.getDriver().findElements(getByLocator()).isEmpty();
    }

    /**
     * Gets the wait handler for this element.
     *
     * @return ElementWait instance
     */
    public ElementWait getWait() {
        return new ElementWait(getByLocator(), alwaysFind);
    }

    /**
     * Waits until the element exists
     * using the default timeout.
     *
     * @return existing web element
     */
    public WebElement waitForExist() {
        return getWait().waitForExist();
    }

    /**
     * Waits until the element exists
     * using the specified timeout.
     *
     * @param timeout maximum time to wait
     * @return existing web element
     */
    public WebElement waitForExist(Duration timeout) {
        return getWait().waitForExist(timeout);
    }

    /**
     * Waits until the element is visible
     * using the default timeout.
     *
     * @return visible web element
     */
    public WebElement waitForVisible() {
        return getWait().waitForVisible();
    }

    /**
     * Waits until the element is visible
     * using the specified timeout.
     *
     * @param timeout maximum time to wait
     * @return visible web element
     */
    public WebElement waitForVisible(Duration timeout) {
        return getWait().waitForVisible(timeout);
    }

    /**
     * Waits until the element is clickable
     * using the default timeout.
     *
     * @return clickable web element
     */
    public WebElement waitForClickable() {
        return getWait().waitForClickable();
    }

    /**
     * Waits until the element is clickable
     * using the specified timeout.
     *
     * @param timeout maximum time to wait
     * @return clickable web element
     */
    public WebElement waitForClickable(Duration timeout) {
        return getWait().waitForClickable(timeout);
    }

    /**
     * Waits until the element is enabled
     * using the default timeout.
     *
     * @return enabled web element
     */
    public WebElement waitForEnabled() {
        return getWait().waitForEnabled();
    }

    /**
     * Waits until the element is enabled
     * using the specified timeout.
     *
     * @param timeout maximum time to wait
     * @return enabled web element
     */
    public WebElement waitForEnabled(Duration timeout) {
        return getWait().waitForEnabled(timeout);
    }

    /**
     * Waits until the element becomes invisible
     * using the default timeout.
     *
     * @return true if the element becomes invisible
     */
    public boolean waitForInvisible() {
        return getWait().waitForInvisible();
    }

    /**
     * Waits until the element becomes invisible
     * using the specified timeout.
     *
     * @param timeout maximum time to wait
     * @return true if the element becomes invisible
     */
    public boolean waitForInvisible(Duration timeout) {
        return getWait().waitForInvisible(timeout);
    }

    /**
     * Waits until the element becomes disabled
     * using the default timeout.
     *
     * @return true if the element becomes disabled
     */
    public boolean waitForDisabled() {
        return getWait().waitForDisabled();
    }

    /**
     * Waits until the element becomes disabled
     * using the specified timeout.
     *
     * @param timeout maximum time to wait
     * @return true if the element becomes disabled
     */
    public boolean waitForDisabled(Duration timeout) {
        return getWait().waitForDisabled(timeout);
    }

    /**
     * Converts the locator string into a Selenium By locator.
     *
     * @return Selenium By locator
     */
    private By getByLocator() {
        String body = locator.replaceAll("[\\w\\s]*=(.*)", "$1").trim();
        String type = locator.replaceAll("([\\w\\s]*)=.*", "$1").trim();
        switch (type) {
            case "css":
                return By.cssSelector(body);
            case "id":
                return By.id(body);
            case "link":
                return By.linkText(body);
            case "xpath":
                return By.xpath(body);
            case "text":
                return By.xpath(
                        String.format("//*[contains(text(), '%s')]", body)
                );
            case "name":
                return By.name(body);
            default:
                return By.xpath(locator);
        }
    }
}