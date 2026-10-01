package com.assertion;

import com.driver.DriverManager;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class AssertRetry {

    private static final List<Class<? extends Throwable>> COMMON_RETRY_EXCEPTIONS = List.of(StaleElementReferenceException.class);

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
        createWait(timeout).until(driver -> condition.evaluate(Duration.ZERO));
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
        createWait(timeout).until(driver -> !condition.evaluate(Duration.ZERO));
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
        assertTrue(() -> Objects.equals(actualSupplier.get(), expected), timeout);
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
        assertTrue(() -> !Objects.equals(actualSupplier.get(), unexpected), timeout);
    }

    /**
     * Creates a WebDriverWait for assertion retry.
     *
     * @param timeout maximum wait duration
     * @return configured WebDriverWait
     */
    private static WebDriverWait createWait(Duration timeout) {
        WebDriverWait wait = new WebDriverWait(
                DriverManager.getDriver(),
                timeout,
                DriverManager.getPollingInterval());
        wait.ignoreAll(COMMON_RETRY_EXCEPTIONS);
        return wait;
    }
}
