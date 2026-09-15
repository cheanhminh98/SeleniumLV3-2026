package com.element;

import org.openqa.selenium.StaleElementReferenceException;

import java.util.NoSuchElementException;

public class ElementConditions {

    /**
     * Checks whether the element exists.
     *
     * @return element existence condition
     */
    public static ElementCondition isExist() {
        return Element::isExist;
    }

    /**
     * Checks whether the element is visible.
     *
     * @return element visibility condition
     */
    public static ElementCondition isVisible() {
        return Element::isDisplayed;
    }

    /**
     * Checks whether the element is enabled.
     *
     * @return element enabled condition
     */
    public static ElementCondition isEnabled() {
        return element -> {
            try {
                return element.getElement().isEnabled();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        };
    }

    /**
     * Checks whether the element is clickable.
     *
     * @return element clickable condition
     */
    public static ElementCondition isClickable() {
        return element -> {
            try {
                return element.isDisplayed() && element.getElement().isEnabled();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        };
    }

    /**
     * Checks whether the element is invisible.
     *
     * @return element invisible condition
     */
    public static ElementCondition isInvisible() {
        return element -> {
            try {
                return !element.isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return true;
            }
        };
    }

    /**
     * Checks whether the element is disabled.
     *
     * @return element disabled condition
     */
    public static ElementCondition isDisabled() {
        return element -> {
            try {
                return !element.getElement().isEnabled();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        };
    }
}
