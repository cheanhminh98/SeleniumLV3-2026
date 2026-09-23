package com.assertion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Assertion {

    private static final ThreadLocal<List<AssertionError>> FAILURES =
            ThreadLocal.withInitial(ArrayList::new);

    /**
     * Asserts that the condition becomes true within the configured timeout.
     *
     * @param condition condition to evaluate
     * @param message failure message
     */
    public static void assertTrue(Supplier<Boolean> condition, String message) {
        Objects.requireNonNull(condition, "Condition cannot be null.");
        try {
            AssertRetry.assertTrue(condition);
        } catch (Exception e) {
            addFailure(
                    new AssertionError(buildMessage(message, "Expected condition to be true."), e)
            );
        }
    }

    /**
     * Asserts that the condition becomes false within the configured timeout.
     *
     * @param condition condition to evaluate
     * @param message failure message
     */
    public static void assertFalse(Supplier<Boolean> condition, String message) {
        Objects.requireNonNull(condition, "Condition cannot be null.");
        try {
            AssertRetry.assertFalse(condition);
        } catch (Exception e) {
            addFailure(
                    new AssertionError(buildMessage(message, "Expected condition to be false."), e)
            );
        }
    }

    /**
     * Asserts that the actual value becomes equal to the expected value
     * within the configured timeout.
     *
     * @param actualSupplier supplier used to retrieve the actual value
     * @param expected expected value
     * @param message failure message
     * @param <T> value type
     */
    public static <T> void assertEquals(Supplier<T> actualSupplier, T expected, String message) {
        Objects.requireNonNull(actualSupplier, "Actual value supplier cannot be null.");
        try {
            AssertRetry.assertEquals(actualSupplier, expected);
        } catch (Exception e) {
            addFailure(
                    new AssertionError(buildMessage(message, "Expected: <" + expected + ">."), e)
            );
        }
    }

    /**
     * Asserts that the actual value becomes different from the
     * unexpected value within the configured timeout.
     *
     * @param actualSupplier supplier used to retrieve the actual value
     * @param unexpected unexpected value
     * @param message failure message
     * @param <T> value type
     */
    public static <T> void assertNotEquals(Supplier<T> actualSupplier, T unexpected, String message) {
        Objects.requireNonNull(actualSupplier, "Actual value supplier cannot be null.");
        try {
            AssertRetry.assertNotEquals(actualSupplier, unexpected);
        } catch (Exception e) {
            addFailure(
                    new AssertionError(
                            buildMessage(message, "Expected value to differ from <" + unexpected + ">."), e)
            );
        }
    }

    /**
     * Reports all collected assertion failures.
     *
     * <p>The failures are aggregated into a single AssertionError
     * and the current thread's assertion state is cleared afterward.</p>
     */
    public static void assertAll() {
        List<AssertionError> failures = FAILURES.get();
        try {
            if (failures.isEmpty()) {
                return;
            }
            String message = failures.stream()
                    .map(AssertionError::getMessage)
                    .collect(Collectors.joining(
                            System.lineSeparator(),
                            "The following assertions failed:"
                                    + System.lineSeparator(),
                            ""
                    ));
            AssertionError aggregatedError = new AssertionError(message);
            failures.forEach(aggregatedError::addSuppressed);
            throw aggregatedError;
        } finally {
            FAILURES.remove();
        }
    }

    /**
     * Adds an assertion failure for the current thread.
     *
     * @param failure assertion failure
     */
    private static void addFailure(AssertionError failure) {
        FAILURES.get().add(failure);
    }

    /**
     * Builds the final assertion failure message.
     *
     * @param message user-defined message
     * @param defaultMessage default assertion message
     * @return combined message
     */
    private static String buildMessage(String message, String defaultMessage) {
        if (message == null || message.isBlank()) {
            return defaultMessage;
        }
        return message + System.lineSeparator() + defaultMessage;
    }
}
