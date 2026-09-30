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
        try {
            AssertRetry.assertTrue(condition);
        } catch (Exception e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(null, "Expected condition to be true."), e));
        }
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
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Expected condition to be true."), e));
        }
    }

    /**
     * Verifies that the condition eventually becomes false.
     *
     * @param condition assertion condition
     */
    public void assertFalse(AssertionCondition condition) {
        try {
            AssertRetry.assertFalse(condition);
        } catch (Exception e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(null, "Expected condition to be false."), e));
        }
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
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Expected condition to be false."), e));
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
     * Verifies that the supplied actual value eventually equals
     * the expected value.
     *
     * @param actualSupplier supplier for the actual value
     * @param expected       expected value
     * @param message        custom assertion message
     * @param <T>            value type
     */
    public <T> void assertEquals(Supplier<T> actualSupplier, T expected, String message) {

        try {
            AssertRetry.assertEquals(actualSupplier, expected);
        } catch (Exception e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Expected: <" + expected + ">."), e));
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
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Values should not be equal: <" + unexpected + ">.")));
        }
    }

    /**
     * Verifies that the supplied actual value eventually differs
     * from the unexpected value.
     *
     * @param actualSupplier supplier for the actual value
     * @param unexpected     unexpected value
     * @param message        custom assertion message
     * @param <T>            value type
     */
    public <T> void assertNotEquals(Supplier<T> actualSupplier, T unexpected, String message) {

        try {
            AssertRetry.assertNotEquals(actualSupplier, unexpected);
        } catch (Exception e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Values should not be equal: <" + unexpected + ">."), e));
        }
    }

    /**
     * Verifies all collected soft assertions.
     */
    public void assertAll() {
        Assertion.assertAll();
    }
}
