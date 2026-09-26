package com.assertion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Assertion {

    private static final ThreadLocal<List<AssertionError>> FAILURES = ThreadLocal.withInitial(ArrayList::new);

    /**
     * Asserts that the condition is true.
     *
     * @param condition condition to verify
     * @param message   assertion message
     */
    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            addFailure(new AssertionError(buildMessage(message, "Expected condition to be true.")));
        }
    }

    /**
     * Asserts that the condition is false.
     *
     * @param condition condition to verify
     * @param message   assertion message
     */
    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            addFailure(new AssertionError(buildMessage(message, "Expected condition to be false.")));
        }
    }

    /**
     * Asserts that actual and expected values are equal.
     *
     * @param actual   actual value
     * @param expected expected value
     * @param message  assertion message
     * @param <T>      value type
     */
    public static <T> void assertEquals(T actual, T expected, String message) {
        if (!Objects.equals(actual, expected)) {
            addFailure(new AssertionError(buildMessage(message, "Expected: <" + expected + ">, but was: <" + actual + ">.")));
        }
    }

    /**
     * Asserts that actual and unexpected values are different.
     *
     * @param actual     actual value
     * @param unexpected unexpected value
     * @param message    assertion message
     * @param <T>        value type
     */
    public static <T> void assertNotEquals(T actual, T unexpected, String message) {
        if (Objects.equals(actual, unexpected)) {
            addFailure(new AssertionError(buildMessage(message, "Expected value to differ from <" + unexpected + ">.")));
        }
    }

    /**
     * Reports all collected assertion failures.
     *
     * <p>The failures are aggregated into one AssertionError.
     * The failure list is cleared after reporting.</p>
     */
    public static void assertAll() {
        List<AssertionError> failures = FAILURES.get();
        try {
            if (failures.isEmpty()) {
                return;
            }
            String message = failures.stream().map(AssertionError::getMessage)
                    .collect(Collectors.joining(System.lineSeparator(),
                            "The following assertions failed:" + System.lineSeparator(), ""));
            AssertionError error = new AssertionError(message);
            failures.forEach(error::addSuppressed);
            throw error;
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
     * Builds the final assertion message.
     *
     * @param message        custom message
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
