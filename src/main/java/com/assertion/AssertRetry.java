package com.assertion;

import com.driver.DriverManager;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

public final class AssertRetry {

    /**
     * Retries until the assertion condition evaluates to true
     * using the default driver timeout.
     *
     * @param condition assertion condition
     */
    public static void assertTrue(AssertionCondition condition) {
        assertTrue(condition, DriverManager.getTimeout());
    }

    /**
     * Retries until the assertion condition evaluates to true.
     *
     * @param condition assertion condition
     * @param timeout   maximum time allowed for the assertion
     */
    public static void assertTrue(AssertionCondition condition, Duration timeout) {
        Objects.requireNonNull(condition, "AssertionCondition cannot be null.");
        Objects.requireNonNull(timeout, "Timeout cannot be null.");
        AssertionRetryContext.setAssertionTimeout(timeout);
        try {
            new AssertionWait(timeout).until(driver -> condition.evaluate());
        } finally {
            AssertionRetryContext.clear();
        }
    }

    /**
     * Retries until the assertion condition evaluates to false
     * using the default driver timeout.
     *
     * @param condition assertion condition
     */
    public static void assertFalse(AssertionCondition condition) {
        assertFalse(condition, DriverManager.getTimeout());
    }

    /**
     * Retries until the assertion condition evaluates to false.
     *
     * @param condition assertion condition
     * @param timeout   maximum time allowed for the assertion
     */
    public static void assertFalse(AssertionCondition condition, Duration timeout) {
        Objects.requireNonNull(condition, "AssertionCondition cannot be null.");
        Objects.requireNonNull(timeout, "Timeout cannot be null.");
        AssertionRetryContext.setAssertionTimeout(timeout);
        try {
            new AssertionWait(timeout).until(driver -> !condition.evaluate());
        } finally {
            AssertionRetryContext.clear();
        }
    }

    /**
     * Retries until the actual value equals the expected value
     * using the default driver timeout.
     *
     * @param actualSupplier supplier for the actual value
     * @param expected       expected value
     * @param <T>            value type
     */
    public static <T> void assertEquals(Supplier<T> actualSupplier, T expected) {
        assertEquals(actualSupplier, expected, DriverManager.getTimeout());
    }

    /**
     * Retries until the actual value equals the expected value.
     *
     * @param actualSupplier supplier for the actual value
     * @param expected       expected value
     * @param timeout        maximum time allowed for the assertion
     * @param <T>            value type
     */
    public static <T> void assertEquals(Supplier<T> actualSupplier, T expected, Duration timeout) {
        Objects.requireNonNull(actualSupplier, "Actual value supplier cannot be null.");
        Objects.requireNonNull(timeout, "Timeout cannot be null.");
        AssertionValue<T> actualValue = new AssertionValue<>();
        AssertionRetryContext.setAssertionTimeout(timeout);
        try {
            new AssertionWait(timeout).until(driver -> {
                T actual = actualSupplier.get();
                actualValue.set(actual);
                return Objects.equals(actual, expected);
            });
        } catch (AssertionTimeoutException e) {
            throw new AssertionTimeoutException("Expected: <" + expected + ">, but got: <" + actualValue.get() + ">.", e);
        } finally {
            AssertionRetryContext.clear();
        }
    }

    /**
     * Retries until the actual value is different from the unexpected value
     * using the default driver timeout.
     *
     * @param actualSupplier supplier for the actual value
     * @param unexpected     value that must not be equal
     * @param <T>            value type
     */
    public static <T> void assertNotEquals(Supplier<T> actualSupplier, T unexpected) {
        assertNotEquals(actualSupplier, unexpected, DriverManager.getTimeout());
    }

    /**
     * Retries until the actual value is different from the unexpected value.
     *
     * @param actualSupplier supplier for the actual value
     * @param unexpected     value that must not be equal
     * @param timeout        maximum time allowed for the assertion
     * @param <T>            value type
     */
    public static <T> void assertNotEquals(Supplier<T> actualSupplier, T unexpected, Duration timeout) {
        Objects.requireNonNull(actualSupplier, "Actual value supplier cannot be null.");
        Objects.requireNonNull(timeout, "Timeout cannot be null.");
        AssertionValue<T> actualValue = new AssertionValue<>();
        AssertionRetryContext.setAssertionTimeout(timeout);
        try {
            new AssertionWait(timeout).until(driver -> {
                T actual = actualSupplier.get();
                actualValue.set(actual);
                return !Objects.equals(actual, unexpected);
            });
        } catch (AssertionTimeoutException e) {
            throw new AssertionTimeoutException("Values should not be equal: <" + unexpected + ">, but got: <" + actualValue.get() + ">.", e);
        } finally {
            AssertionRetryContext.clear();
        }
    }

    /**
     * Stores the latest actual value evaluated during polling.
     *
     * @param <T> value type
     */
    private static final class AssertionValue<T> {

        private T value;

        void set(T value) {
            this.value = value;
        }

        T get() {
            return value;
        }
    }
}
