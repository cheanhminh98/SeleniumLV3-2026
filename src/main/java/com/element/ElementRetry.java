package com.element;

import org.openqa.selenium.WebDriver;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

class ElementRetry {

    private final ElementWait wait;

    /**
     * Creates an ElementRetry using the specified ElementWait.
     *
     * @param wait ElementWait used for retry operations
     */
    ElementRetry(ElementWait wait) {
        Objects.requireNonNull(wait, "ElementWait cannot be null.");
        this.wait = wait;
    }

    /**
     * Retries the specified action.
     *
     * @param action action to execute
     */
    void retryAction(Runnable action) {
        retryAction(action, Collections.emptyList());
    }

    /**
     * Retries the specified action with additional exceptions.
     *
     * @param action               action to execute
     * @param additionalExceptions additional retry exceptions
     */
    void retryAction(Runnable action, List<Class<? extends Throwable>> additionalExceptions) {
        wait.until(additionalExceptions, (WebDriver driver) -> {
            action.run();
            return true;
        });
    }

    /**
     * Retries the specified action and returns its result.
     *
     * @param action action to execute
     * @param <T>    result type
     * @return action result
     */
    <T> T retryAction(Supplier<T> action) {
        return retryAction(action, Collections.emptyList());
    }

    /**
     * Retries the specified action with additional exceptions.
     *
     * @param action               action to execute
     * @param additionalExceptions additional retry exceptions
     * @param <T>                  result type
     * @return action result
     */
    <T> T retryAction(Supplier<T> action, List<Class<? extends Throwable>> additionalExceptions) {
        return wait.until(additionalExceptions, (WebDriver driver) -> action.get());
    }
}