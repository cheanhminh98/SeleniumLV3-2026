package com.assertion;

import com.driver.DriverManager;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class AssertRetry {

    private static final List<Class<? extends Throwable>> COMMON_RETRY_EXCEPTIONS = List.of(StaleElementReferenceException.class);

    /**
     * Retries the condition until it evaluates to true
     * or the configured timeout is reached.
     *
     * @param condition assertion condition
     */
    public static void assertTrue(AssertionCondition condition) {
        Objects.requireNonNull(condition, "AssertionCondition cannot be null.");
        createWait().until(driver -> condition.evaluate());
    }

    /**
     * Retries the condition until it evaluates to false
     * or the configured timeout is reached.
     *
     * @param condition assertion condition
     */
    public static void assertFalse(AssertionCondition condition) {
        Objects.requireNonNull(condition, "AssertionCondition cannot be null.");
        createWait().until(driver -> !condition.evaluate());
    }

    /**
     * Retries until the supplied actual value equals
     * the expected value.
     *
     * @param actualSupplier supplier used to obtain the actual value
     * @param expected       expected value
     * @param <T>            value type
     */
    public static <T> void assertEquals(Supplier<T> actualSupplier, T expected) {
        Objects.requireNonNull(actualSupplier, "Actual value supplier cannot be null.");
        createWait().until(driver -> Objects.equals(actualSupplier.get(), expected));
    }

    /**
     * Retries until the supplied actual value differs
     * from the unexpected value.
     *
     * @param actualSupplier supplier used to obtain the actual value
     * @param unexpected     unexpected value
     * @param <T>            value type
     */
    public static <T> void assertNotEquals(Supplier<T> actualSupplier, T unexpected) {
        Objects.requireNonNull(actualSupplier, "Actual value supplier cannot be null.");
        createWait().until(driver -> !Objects.equals(actualSupplier.get(), unexpected));
    }

    /**
     * Creates a WebDriverWait using the timeout and polling
     * configuration from DriverManager.
     *
     * @return configured WebDriverWait
     */
    private static WebDriverWait createWait() {
        WebDriverWait wait = new WebDriverWait(
                DriverManager.getDriver(),
                DriverManager.getTimeout(),
                DriverManager.getPollingInterval());
        wait.ignoreAll(COMMON_RETRY_EXCEPTIONS);
        return wait;
    }
}
