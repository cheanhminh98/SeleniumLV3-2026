package com.assertion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SoftAssertion {

    private final List<AssertionError> failures = new ArrayList<>();

    /**
     * Asserts that the condition eventually evaluates to true.
     *
     * @param condition assertion condition
     * @param message   assertion message
     */
    public void assertTrue(AssertionCondition condition, String message) {
        Objects.requireNonNull(condition, "AssertionCondition cannot be null.");
        try {
            AssertRetry.assertTrue(condition);
        } catch (Exception e) {
            addFailure(new AssertionError(Assertion.buildMessage(message, "Expected condition to be true."), e));
        }
    }

    /**
     * Asserts that the condition eventually evaluates to false.
     *
     * @param condition assertion condition
     * @param message   assertion message
     */
    public void assertFalse(AssertionCondition condition, String message) {
        Objects.requireNonNull(condition, "AssertionCondition cannot be null.");
        try {
            AssertRetry.assertFalse(condition);
        } catch (Exception e) {
            addFailure(new AssertionError(Assertion.buildMessage(message, "Expected condition to be false."), e));
        }
    }

    /**
     * Asserts that the supplied value eventually equals
     * the expected value.
     *
     * @param actualSupplier supplier for the actual value
     * @param expected       expected value
     * @param message        assertion message
     * @param <T>            value type
     */
    public <T> void assertEquals(Supplier<T> actualSupplier, T expected, String message) {
        Objects.requireNonNull(actualSupplier, "Actual value supplier cannot be null.");
        try {
            AssertRetry.assertEquals(actualSupplier, expected);
        } catch (Exception e) {
            addFailure(new AssertionError(Assertion.buildMessage(message, "Expected: <" + expected + ">."), e));
        }
    }

    /**
     * Asserts that the supplied value eventually differs
     * from the unexpected value.
     *
     * @param actualSupplier supplier for the actual value
     * @param unexpected     unexpected value
     * @param message        assertion message
     * @param <T>            value type
     */
    public <T> void assertNotEquals(Supplier<T> actualSupplier, T unexpected, String message) {
        Objects.requireNonNull(actualSupplier, "Actual value supplier cannot be null.");
        try {
            AssertRetry.assertNotEquals(actualSupplier, unexpected);
        } catch (Exception e) {
            addFailure(new AssertionError(Assertion.buildMessage(message, "Expected value to differ from <" + unexpected + ">."), e));
        }
    }

    /**
     * Throws one AssertionError containing all collected failures.
     */
    public void assertAll() {
        if (failures.isEmpty()) {
            return;
        }
        String message = failures.stream().map(
                AssertionError::getMessage)
                .collect(Collectors.joining(System.lineSeparator(),
                        "The following assertions failed:"
                                + System.lineSeparator(), ""));
        AssertionError error = new AssertionError(message);
        failures.forEach(error::addSuppressed);
        throw error;
    }

    /**
     * Clears all collected assertion failures.
     */
    public void clear() {
        failures.clear();
    }

    /**
     * Adds an assertion failure.
     *
     * @param failure assertion failure
     */
    private void addFailure(AssertionError failure) {
        failures.add(failure);
    }
}
