package com.assertion;

import java.time.Duration;
import java.util.Objects;
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
            Assertion.addFailure(message, "Condition should be true.", e);
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
            Assertion.addFailure(message, "Condition should be true.", e);
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
            Assertion.addFailure(message, "Condition should be false.", e);
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
            Assertion.addFailure(message, "Condition should be false.", e);
        }
    }

    /**
     * Asserts that the actual value equals the expected value.
     * This is a snapshot assertion and does not retry.
     *
     * @param actual   actual value
     * @param expected expected value
     * @param message  custom assertion message
     * @param <T>      value type
     */
    public <T> void assertEquals(T actual, T expected, String message) {
        if (!Objects.equals(actual, expected)) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Expected: <" + expected + ">, but was: <" + actual + ">.")));
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
            Assertion.addFailure(message, "Values should be equal.", e);
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
            Assertion.addFailure(message, "Values should be equal.", e);
        }
    }

    /**
     * Asserts that the actual value is different from the unexpected value.
     * This is a snapshot assertion and does not retry.
     *
     * @param actual     actual value
     * @param unexpected unexpected value
     * @param message    custom assertion message
     * @param <T>        type of the assertion value
     */
    public <T> void assertNotEquals(T actual, T unexpected, String message) {
        if (Objects.equals(actual, unexpected)) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Values should not be equal: <" + unexpected + ">.")));
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
            Assertion.addFailure(message, "Values should not be equal.", e);
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
            Assertion.addFailure(message, "Values should not be equal: <" + unexpected + ">.", e);
        }
    }

    /**
     * Asserts that the boolean condition is true.
     * This is a snapshot assertion and does not retry.
     *
     * @param condition condition to evaluate
     * @param message   assertion failure message
     */
    public void assertTrue(boolean condition, String message) {
        if (!condition) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Expected condition to be true.")));
        }
    }

    /**
     * Asserts that the boolean condition is false.
     * This is a snapshot assertion and does not retry.
     *
     * @param condition condition to evaluate
     * @param message   assertion failure message
     */
    public void assertFalse(boolean condition, String message) {
        if (condition) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Expected condition to be false.")));
        }
    }

    /**
     * Asserts that the actual boolean value equals the expected value.
     * This is a snapshot assertion and does not retry.
     *
     * @param actual   actual boolean value
     * @param expected expected boolean value
     * @param message  assertion failure message
     */
    public void assertEquals(boolean actual, boolean expected, String message) {
        if (actual != expected) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Expected: <" + expected + ">, but was: <" + actual + ">.")));
        }
    }
}
