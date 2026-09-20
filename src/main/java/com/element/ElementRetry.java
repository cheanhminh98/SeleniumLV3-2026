package com.element;

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
     * @param action action to execute
     * @param additionalExceptions additional exceptions to ignore
     */
    void retryAction(Runnable action, List<Class<? extends Throwable>> additionalExceptions) {
        Objects.requireNonNull(action, "Action cannot be null.");
        Objects.requireNonNull(additionalExceptions, "Additional exceptions cannot be null.");
        wait.createWait(additionalExceptions)
                .until(driver -> {
                    action.run();
                    return true;
                });
    }


    /**
     * Retries an action until it executes successfully and returns
     * its result.
     * A null result is considered a valid successful result.
     *
     * @param action action to execute
     * @param <T> result type
     * @return action result, including null
     */
    <T> T retryValue(Supplier<T> action) {
        return retryValue(action, Collections.emptyList());
    }

    /**
     * Retries an action until it executes successfully and returns
     * its result.
     * The result is wrapped so that a successful action returning
     * null is still treated as a successful WebDriverWait result.
     *
     * @param action action to execute
     * @param additionalExceptions additional exceptions to ignore
     * @param <T> result type
     * @return action result, including null
     */
    <T> T retryValue(Supplier<T> action, List<Class<? extends Throwable>> additionalExceptions) {
        Objects.requireNonNull(action, "Action cannot be null.");
        Objects.requireNonNull(additionalExceptions, "Additional exceptions cannot be null.");
        RetryResult<T> result = wait
                .createWait(additionalExceptions)
                .until(driver -> new RetryResult<>(action.get()));
        return result.getValue();
    }

    /**
     * Wraps an action result so WebDriverWait can distinguish
     * a successful null result from an unsuccessful wait condition.
     *
     * @param <T> result type
     */
    private static final class RetryResult<T> {
        private final T value;

        /**
         * Creates a retry result.
         *
         * @param value action result, which may be null
         */
        private RetryResult(T value) {
            this.value = value;
        }

        /**
         * Gets the action result.
         *
         * @return action result, including null
         */
        private T getValue() {
            return value;
        }
    }
}
