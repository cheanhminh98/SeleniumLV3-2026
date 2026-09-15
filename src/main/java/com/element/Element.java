package com.element;

import com.driver.DriverManager;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Element {

    private final By locator;

    @Getter
    private final ElementWait wait = new ElementWait(this);
    private final ElementRetry retry = new ElementRetry(wait);

    private static final List<Class<? extends Throwable>> INTERACTION_RETRY_EXCEPTIONS =
            List.of(
                    ElementClickInterceptedException.class,
                    ElementNotInteractableException.class
            );

    private static final List<Class<? extends Throwable>> INPUT_RETRY_EXCEPTIONS =
            List.of(
                    ElementNotInteractableException.class
            );

    /**
     * Creates an Element from a locator string.
     *
     * @param locator locator string
     */
    public Element(String locator) {
        if (locator == null || locator.trim().isEmpty()) {
            throw new IllegalArgumentException("Locator cannot be null or empty.");
        }
        this.locator = getByLocator(locator);
    }

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
        retry.retryAction(() -> {
            WebElement webElement = getElement();
            webElement.clear();
            webElement.sendKeys(value);
            }, INPUT_RETRY_EXCEPTIONS
        );
    }

    /**
     * Sends a value to the element without clearing it first.
     *
     * @param value value to enter
     */
    public void enter(String value) {
        retry.retryAction(() -> getElement().sendKeys(value), INPUT_RETRY_EXCEPTIONS);
    }

    /**
     * Clicks the element.
     */
    public void click() {
        retry.retryAction(() -> getElement().click(), INTERACTION_RETRY_EXCEPTIONS);
    }

    /**
     * Checks the element if it is not already selected.
     */
    public void check() {
        retry.retryAction(() -> {
            WebElement webElement = getElement();
            if (!webElement.isSelected()) {
                webElement.click();
                 }
            }, INTERACTION_RETRY_EXCEPTIONS
        );
    }

    /**
     * Moves the mouse over the element.
     */
    public void hover() {
        retry.retryAction(() -> new Actions(
                DriverManager.getDriver())
                        .moveToElement(getElement())
                        .perform()
        );
    }

    /**
     * Scrolls the element into view.
     */
    public void scrollToView() {
        retry.retryAction(() -> {
            WebElement webElement = getElement();
            ((JavascriptExecutor)
                    DriverManager.getDriver())
                    .executeScript("arguments[0].scrollIntoView({block: 'center'});", webElement);
            });
    }

    /**
     * Gets the visible text of the element.
     *
     * @return element text
     */
    public String getText() {
        return retry.retryAction(() -> getElement().getText());
    }

    /**
     * Gets the value attribute of the element.
     *
     * @return element value
     */
    public String getValue() {
        return retry.retryAction(() -> getElement().getAttribute("value"));
    }

    /**
     * Converts a locator string into a Selenium By locator.
     *
     * <p>
     * Supported formats:
     * css=...
     * id=...
     * link=...
     * xpath=...
     * text=...
     * name=...
     * </p>
     *
     * @param locator locator string
     * @return Selenium By locator
     */
    private static By getByLocator(String locator) {
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
                return By.xpath(String.format("//*[contains(text(), '%s')]", body));
            case "name":
                return By.name(body);
            default:
                return By.xpath(locator);
        }
    }

    /**
     * Checks whether the element is displayed.
     *
     * <p>
     * This is an immediate state check and does not wait.
     * </p>
     *
     * @return true if the element exists and is displayed
     */
    public boolean isDisplayed() {
        List<WebElement> elements = DriverManager.getDriver().findElements(locator);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    /**
     * Checks whether the element exists.
     *
     * <p>
     * This is an immediate state check and does not wait.
     * </p>
     *
     * @return true if at least one matching element exists
     */
    public boolean isExist() {
        return !DriverManager.getDriver().findElements(locator).isEmpty();
    }

    /**
     * Waits until the element exists.
     *
     * @return existing WebElement
     */
    public WebElement waitForExist() {
        wait.until(ElementConditions.isExist());
        return getElement();
    }

    /**
     * Waits until the element is visible.
     *
     * @return visible WebElement
     */
    public WebElement waitForVisible() {
        wait.until(ElementConditions.isVisible());
        return getElement();
    }

    /**
     * Waits until the element is clickable.
     *
     * @return clickable WebElement
     */
    public WebElement waitForClickable() {
        wait.until(ElementConditions.isClickable());
        return getElement();
    }

    /**
     * Waits until the element is enabled.
     *
     * @return enabled WebElement
     */
    public WebElement waitForEnabled() {
        wait.until(ElementConditions.isEnabled());
        return getElement();
    }

    /**
     * Waits until the element becomes invisible.
     *
     * @return true when the element is invisible
     */
    public boolean waitForInvisible() {
        wait.until(ElementConditions.isInvisible());
        return true;
    }

    /**
     * Waits until the element becomes disabled.
     *
     * @return true when the element is disabled
     */
    public boolean waitForDisabled() {
        wait.until(ElementConditions.isDisabled());
        return true;
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