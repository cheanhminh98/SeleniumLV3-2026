package com.assertion;

import java.util.Objects;
import java.util.function.Supplier;

public class SoftAssertion {

    /**
     * Verifies that the condition eventually becomes true.
     *
     * @param condition assertion condition
     */
    public void assertTrue(AssertionCondition condition) {
        assertTrue(condition, null);
    }

    /**
     * Verifies that the condition eventually becomes true.
     *
     * @param condition assertion condition
     * @param message   custom assertion message
     */
    public void assertTrue(AssertionCondition condition, String message) {
        try {
            AssertRetry.assertTrue(condition);
        } catch (Exception e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message,
                    "Expected condition to be true.", e), e));
        }
    }

    /**
     * Verifies that the condition eventually becomes false.
     *
     * @param condition assertion condition
     */
    public void assertFalse(AssertionCondition condition) {
        assertFalse(condition, null);
    }

    /**
     * Verifies that the condition eventually becomes false.
     *
     * @param condition assertion condition
     * @param message   custom assertion message
     */
    public void assertFalse(AssertionCondition condition, String message) {
        try {
            AssertRetry.assertFalse(condition);
        } catch (Exception e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message,
                    "Expected condition to be false.", e), e));
        }
    }

    /**
     * Verifies that two values are equal.
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
     * Retries until the supplied actual value equals
     * the expected value.
     *
     * @param actualSupplier supplier used to obtain the actual value
     * @param expected       expected value
     * @param message        custom assertion message
     * @param <T>            value type
     */
    public <T> void assertEquals(Supplier<T> actualSupplier, T expected, String message) {

        try {
            AssertRetry.assertEquals(actualSupplier, expected);
        } catch (Exception e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message,
                    "Expected: <" + expected + ">.", e), e));
        }
    }

    /**
     * Verifies that two values are not equal.
     *
     * @param actual     actual value
     * @param unexpected unexpected value
     * @param message    custom assertion message
     * @param <T>        value type
     */
    public <T> void assertNotEquals(T actual, T unexpected, String message) {
        if (Objects.equals(actual, unexpected)) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message,
                    "Values should not be equal: <" + unexpected + ">.")));
        }
    }

    /**
     * Retries until the supplied actual value differs
     * from the unexpected value.
     *
     * @param actualSupplier supplier used to obtain the actual value
     * @param unexpected     unexpected value
     * @param message        custom assertion message
     * @param <T>            value type
     */
    public <T> void assertNotEquals(Supplier<T> actualSupplier, T unexpected, String message) {
        try {
            AssertRetry.assertNotEquals(actualSupplier, unexpected);
        } catch (Exception e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message,
                    "Values should not be equal: <" + unexpected + ">.", e)));
        }
    }

    /**
     * Verifies all collected soft assertions.
     */
    public void assertAll() {
        Assertion.assertAll();
    }
}
