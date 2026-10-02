package com.assertion;

import org.openqa.selenium.TimeoutException;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

public class HardAssertion {

    /**
     * Verifies that the specified condition eventually evaluates to
     * using the default assertion timeout.
     *
     * @param condition assertion condition to evaluate
     * @throws AssertionError when the condition does not become true
     */
    public void assertTrue(AssertionCondition condition, String message) {

        try {
            AssertRetry.assertTrue(condition);
        } catch (TimeoutException e) {
            throw new AssertionError(Assertion.buildMessage(message, "Expected condition to be true.", e), e);
        }
    }

    /**
     * Verifies that the specified condition eventually evaluates to
     * within the specified timeout.
     *
     * @param condition assertion condition to evaluate
     * @param timeout   maximum time allowed for the assertion
     * @param message   custom assertion message
     * @throws AssertionError when the condition does not become true
     */
    public void assertTrue(AssertionCondition condition, Duration timeout, String message) {

        try {
            AssertRetry.assertTrue(condition, timeout);
        } catch (TimeoutException e) {
            throw new AssertionError(Assertion.buildMessage(message, "Expected condition to be true.", e), e);
        }
    }

    /**
     * Verifies that the specified condition eventually evaluates to
     * using the default assertion timeout.
     *
     * @param condition assertion condition to evaluate
     * @param message   custom assertion message
     * @throws AssertionError when the condition does not become false
     */
    public void assertFalse(AssertionCondition condition, String message) {
        try {
            AssertRetry.assertFalse(condition);
        } catch (TimeoutException e) {
            throw new AssertionError(Assertion.buildMessage(message, "Expected condition to be false.", e), e);
        }
    }

    /**
     * Verifies that the specified condition eventually evaluates to
     * within the specified timeout.
     *
     * @param condition assertion condition to evaluate
     * @param timeout   maximum time allowed for the assertion
     * @param message   custom assertion message
     * @throws AssertionError when the condition does not become false
     */
    public void assertFalse(AssertionCondition condition, Duration timeout, String message) {
        try {
            AssertRetry.assertFalse(condition, timeout);
        } catch (TimeoutException e) {
            throw new AssertionError(Assertion.buildMessage(message, "Expected condition to be false.", e), e);
        }
    }

    /**
     * Verifies that the actual value equals the expected value.
     * This is a snapshot assertion and does not retry.
     *
     * @param actual   actual value
     * @param expected expected value
     * @param message  custom assertion message
     * @param <T>      value type
     * @throws AssertionError when the values are not equal
     */
    public <T> void assertEquals(T actual, T expected, String message) {
        if (!Objects.equals(actual, expected)) {
            throw new AssertionError(Assertion.buildMessage(message, "Expected: <" + expected + ">, but was: <" + actual + ">."));
        }
    }

    /**
     * Verifies that the supplied actual value eventually equals
     * the expected value within the specified timeout.
     * The supplier is evaluated repeatedly until the assertion
     * passes or the timeout is reached.
     *
     * @param actualSupplier supplier that provides the actual value
     * @param expected       expected value
     * @param timeout        maximum time allowed for the assertion
     * @param message        custom assertion message
     * @param <T>            value type
     * @throws AssertionError when the actual value does not equal
     *                        the expected value within the timeout
     */
    public <T> void assertEquals(Supplier<T> actualSupplier, T expected, Duration timeout, String message) {
        try {
            AssertRetry.assertEquals(actualSupplier, expected, timeout);
        } catch (TimeoutException e) {
            throw new AssertionError(Assertion.buildMessage(message, "Expected: <" + expected + ">.", e), e);
        }
    }

    /**
     * Verifies that the actual value is different from the unexpected value.
     * This is a snapshot assertion and does not retry.
     *
     * @param actual     actual value
     * @param unexpected value that the actual value must not equal
     * @param message    custom assertion message
     * @param <T>        value type
     * @throws AssertionError when the values are equal
     */
    public <T> void assertNotEquals(T actual, T unexpected, String message) {
        if (Objects.equals(actual, unexpected)) {
            throw new AssertionError(Assertion.buildMessage(message, "Values should not be equal: <" + unexpected + ">."));
        }
    }

    /**
     * Verifies that the supplied actual value eventually differs
     * from the unexpected value within the specified timeout.
     * The supplier is evaluated repeatedly until the assertion
     * passes or the timeout is reached.
     *
     * @param actualSupplier supplier that provides the actual value
     * @param unexpected     value that the actual value must not equal
     * @param timeout        maximum time allowed for the assertion
     * @param message        custom assertion message
     * @param <T>            value type
     * @throws AssertionError when the actual value remains equal
     *                        to the unexpected value until the timeout
     */
    public <T> void assertNotEquals(Supplier<T> actualSupplier, T unexpected, Duration timeout, String message) {
        try {
            AssertRetry.assertNotEquals(actualSupplier, unexpected, timeout);
        } catch (TimeoutException e) {
            throw new AssertionError(Assertion.buildMessage(message, "Values should not be equal: <" + unexpected + ">.", e), e);
        }
    }
}
