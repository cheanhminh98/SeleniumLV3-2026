package com.element;

import org.openqa.selenium.WebElement;

import java.util.List;

public class ElementConditions {

    /**
     * Checks whether the element exists.
     *
     * @return element existence condition
     */
    public static ElementCondition isExist() {
        return element -> !element.getElements().isEmpty();
    }

    /**
     * Checks whether the element is visible.
     *
     * @return element visibility condition
     */
    public static ElementCondition isVisible() {
        return element -> {
            List<WebElement> elements = element.getElements();
            return !elements.isEmpty()
                    && elements.get(0).isDisplayed();
        };
    }

    /**
     * Checks whether the element is enabled.
     *
     * @return element enabled condition
     */
    public static ElementCondition isEnabled() {
        return element -> {
            List<WebElement> elements = element.getElements();
            return !elements.isEmpty()
                    && elements.get(0).isEnabled();
        };
    }

    /**
     * Checks whether the element is clickable.
     *
     * @return element clickable condition
     */
    public static ElementCondition isClickable() {
        return element -> {
            List<WebElement> elements = element.getElements();
            if (elements.isEmpty()) {
                return false;
            }
            WebElement webElement = elements.get(0);
            return webElement.isDisplayed()
                    && webElement.isEnabled();
        };
    }

    /**
     * Checks whether the element is invisible.
     *
     * @return element invisible condition
     */
    public static ElementCondition isInvisible() {
        return element -> {
            List<WebElement> elements = element.getElements();
            return elements.isEmpty()
                    || !elements.get(0).isDisplayed();
        };
    }

    /**
     * Checks whether the element is disabled.
     *
     * @return element disabled condition
     */
    public static ElementCondition isDisabled() {
        return element -> {
            List<WebElement> elements = element.getElements();
            return !elements.isEmpty()
                    && !elements.get(0).isEnabled();
        };
    }
}
