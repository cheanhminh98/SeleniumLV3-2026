package com.assertion;

import java.util.function.Supplier;

public class HardAssertion {

    /**
     * Asserts that the condition eventually evaluates to true.
     *
     * @param condition assertion condition
     * @param message   assertion message
     */
    public static void assertTrue(AssertionCondition condition, String message) {
        try {
            AssertRetry.assertTrue(condition);
        } catch (Exception e) {
            throw new AssertionError(Assertion.buildMessage(message, "Expected condition to be true."), e);
        }
    }

    /**
     * Asserts that the condition eventually evaluates to false.
     *
     * @param condition assertion condition
     * @param message   assertion message
     */
    public static void assertFalse(AssertionCondition condition, String message) {
        try {
            AssertRetry.assertFalse(condition);
        } catch (Exception e) {
            throw new AssertionError(Assertion.buildMessage(message, "Expected condition to be false."), e);
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
    public static <T> void assertEquals(Supplier<T> actualSupplier, T expected, String message) {
        try {
            AssertRetry.assertEquals(actualSupplier, expected);
        } catch (Exception e) {
            throw new AssertionError(Assertion.buildMessage(message, "Expected: <" + expected + ">."), e);
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
    public static <T> void assertNotEquals(Supplier<T> actualSupplier, T unexpected, String message) {
        try {
            AssertRetry.assertNotEquals(actualSupplier, unexpected);
        } catch (Exception e) {
            throw new AssertionError(Assertion.buildMessage(message, "Expected value to differ from <" + unexpected + ">."), e);
        }
    }
}
