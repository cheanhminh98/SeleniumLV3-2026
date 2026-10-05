package com.assertion;

import java.time.Duration;
import java.util.function.Supplier;

public class SoftAssertion {

    /**
     * Asserts that the condition eventually becomes true
     * using the default timeout.
     *
     * @param condition assertion condition
     * @param message   custom assertion message
     */
    public void assertTrue(AssertionCondition condition, String message) {
        try {
            AssertRetry.assertTrue(condition);
        } catch (AssertionTimeoutException e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Condition should be true.", e)));
        }
    }

    /**
     * Asserts that the condition eventually becomes true.
     *
     * @param condition assertion condition
     * @param timeout   assertion timeout
     * @param message   custom assertion message
     */
    public void assertTrue(AssertionCondition condition, Duration timeout, String message) {
        try {
            AssertRetry.assertTrue(condition, timeout);
        } catch (AssertionTimeoutException e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Condition should be true.", e)));
        }
    }

    /**
     * Asserts that the condition eventually becomes false
     * using the default timeout.
     *
     * @param condition assertion condition
     * @param message   custom assertion message
     */
    public void assertFalse(AssertionCondition condition, String message) {
        try {
            AssertRetry.assertFalse(condition);
        } catch (AssertionTimeoutException e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Condition should be false.", e)));
        }
    }

    /**
     * Asserts that the condition eventually becomes false.
     *
     * @param condition assertion condition
     * @param timeout   assertion timeout
     * @param message   custom assertion message
     */
    public void assertFalse(AssertionCondition condition, Duration timeout, String message) {
        try {
            AssertRetry.assertFalse(condition, timeout);
        } catch (AssertionTimeoutException e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Condition should be false.", e)));
        }
    }

    /**
     * Asserts that the supplied actual value eventually equals
     * the expected value using the default timeout.
     *
     * @param actualSupplier supplier that returns the actual value
     * @param expected       expected value
     * @param message        custom assertion message
     * @param <T>            type of the assertion value
     */
    public <T> void assertEquals(Supplier<T> actualSupplier, T expected, String message) {
        try {
            AssertRetry.assertEquals(actualSupplier, expected);
        } catch (AssertionTimeoutException e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Values should be equal.", e)));
        }
    }

    /**
     * Asserts that the supplied actual value eventually equals
     * the expected value.
     *
     * @param actualSupplier supplier that returns the actual value
     * @param expected       expected value
     * @param timeout        assertion timeout
     * @param message        custom assertion message
     * @param <T>            type of the assertion value
     */
    public <T> void assertEquals(Supplier<T> actualSupplier, T expected, Duration timeout, String message) {
        try {
            AssertRetry.assertEquals(actualSupplier, expected, timeout);
        } catch (AssertionTimeoutException e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Values should be equal.", e)));
        }
    }

    /**
     * Asserts that the supplied actual value eventually differs
     * from the unexpected value using the default timeout.
     *
     * @param actualSupplier supplier that returns the actual value
     * @param unexpected     unexpected value
     * @param message        custom assertion message
     * @param <T>            type of the assertion value
     */
    public <T> void assertNotEquals(Supplier<T> actualSupplier, T unexpected, String message) {
        try {
            AssertRetry.assertNotEquals(actualSupplier, unexpected);
        } catch (AssertionTimeoutException e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Values should not be equal.", e)));
        }
    }

    /**
     * Asserts that the supplied actual value eventually differs
     * from the unexpected value.
     *
     * @param actualSupplier supplier that returns the actual value
     * @param unexpected     unexpected value
     * @param timeout        assertion timeout
     * @param message        custom assertion message
     * @param <T>            type of the assertion value
     */
    public <T> void assertNotEquals(Supplier<T> actualSupplier, T unexpected, Duration timeout, String message) {
        try {
            AssertRetry.assertNotEquals(actualSupplier, unexpected, timeout);
        } catch (AssertionTimeoutException e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Values should not be equal.", e)));
        }
    }
}
