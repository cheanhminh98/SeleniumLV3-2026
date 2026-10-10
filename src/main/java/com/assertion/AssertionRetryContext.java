package com.assertion;

import java.time.Duration;

public class AssertionRetryContext {
    private static final ThreadLocal<Duration> ASSERTION_TIMEOUT = ThreadLocal.withInitial(() -> null);

    /**
     * Sets the current assertion timeout.
     *
     * @param timeout current assertion timeout
     */
    public static void setAssertionTimeout(Duration timeout) {
        ASSERTION_TIMEOUT.set(timeout);
    }

    /**
     * Gets the current assertion timeout.
     *
     * @return assertion timeout, or null if not in assertion context
     */
    public static Duration getAssertionTimeout() {
        return ASSERTION_TIMEOUT.get();
    }

    /**
     * Clears the assertion timeout context.
     */
    public static void clear() {
        ASSERTION_TIMEOUT.remove();
    }

    /**
     * Checks if currently inside an assertion wait.
     *
     * @return true if inside assertion context
     */
    public static boolean isInAssertionContext() {
        return ASSERTION_TIMEOUT.get() != null;
    }
}
