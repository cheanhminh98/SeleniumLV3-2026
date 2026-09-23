package com.assertion;

import com.driver.DriverManager;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class AssertRetry {

    private static final List<Class<? extends Throwable>> COMMON_RETRY_EXCEPTIONS = List.of(
            StaleElementReferenceException.class
    );

    /**
     * Retries a condition until it becomes true.
     *
     * @param condition condition to evaluate
     */
    static void assertTrue(Supplier<Boolean> condition) {
        Objects.requireNonNull(condition, "Condition cannot be null.");
        createWait().until(driver ->
                Boolean.TRUE.equals(condition.get())
        );
    }

    /**
     * Retries a condition until it becomes false.
     *
     * @param condition condition to evaluate
     */
    static void assertFalse(Supplier<Boolean> condition) {
        Objects.requireNonNull(condition, "Condition cannot be null.");
        createWait().until(driver ->
                !Boolean.TRUE.equals(condition.get())
        );
    }

    /**
     * Retries the actual value until it equals the expected value.
     *
     * @param actualSupplier supplier used to retrieve the actual value
     * @param expected expected value
     * @param <T> value type
     */
    static <T> void assertEquals(Supplier<T> actualSupplier, T expected) {
        Objects.requireNonNull(actualSupplier, "Actual value supplier cannot be null.");
        createWait().until(driver ->
                Objects.equals(
                        actualSupplier.get(),
                        expected
                )
        );
    }

    /**
     * Retries the actual value until it differs from the unexpected value.
     *
     * @param actualSupplier supplier used to retrieve the actual value
     * @param unexpected unexpected value
     * @param <T> value type
     */
    static <T> void assertNotEquals(Supplier<T> actualSupplier, T unexpected) {
        Objects.requireNonNull(actualSupplier, "Actual value supplier cannot be null.");
        createWait().until(driver ->
                !Objects.equals(
                        actualSupplier.get(),
                        unexpected
                )
        );
    }

    /**
     * Creates a WebDriverWait using the timeout and polling interval
     * configured in DriverManager.
     *
     * @return new WebDriverWait for the current assertion
     */
    private static WebDriverWait createWait() {
        WebDriverWait wait = new WebDriverWait(
                DriverManager.getDriver(),
                DriverManager.getTimeout(),
                DriverManager.getPollingInterval()
        );
        wait.ignoreAll(COMMON_RETRY_EXCEPTIONS);
        return wait;
    }
}
