package com.assertion;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

public class SoftAssertion {

    /**
     * Verifies that the specified condition eventually evaluates to
     * using the default assertion timeout.
     * If the condition does not become true within the timeout,
     * the failure is collected instead of being thrown immediately.
     *
     * @param condition assertion condition to evaluate
     * @param message   custom assertion message
     */
    public void assertTrue(AssertionCondition condition, String message) {
        try {
            AssertRetry.assertTrue(condition);
        } catch (AssertionTimeoutException e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Expected condition to be true.", e), e));
        }
    }

    /**
     * Verifies that the specified condition eventually evaluates to
     * within the specified timeout.
     * If the condition does not become true within the timeout,
     * the failure is collected instead of being thrown immediately.
     *
     * @param condition assertion condition to evaluate
     * @param timeout   maximum time allowed for the assertion
     * @param message   custom assertion message
     */
    public void assertTrue(AssertionCondition condition, Duration timeout, String message) {
        try {
            AssertRetry.assertTrue(condition, timeout);
        } catch (AssertionTimeoutException  e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Expected condition to be true.", e), e));
        }
    }

    /**
     * Verifies that the specified condition eventually evaluates to
     * using the default assertion timeout.
     * If the condition does not become false within the timeout,
     * the failure is collected instead of being thrown immediately.
     *
     * @param condition assertion condition to evaluate
     * @param message   custom assertion message
     */
    public void assertFalse(AssertionCondition condition, String message) {
        try {
            AssertRetry.assertFalse(condition);
        } catch (AssertionTimeoutException e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Expected condition to be false.", e), e));
        }
    }

    /**
     * Verifies that the specified condition eventually evaluates to
     * within the specified timeout.
     * If the condition does not become false within the timeout,
     * the failure is collected instead of being thrown immediately.
     *
     * @param condition assertion condition to evaluate
     * @param timeout   maximum time allowed for the assertion
     * @param message   custom assertion message
     */
    public void assertFalse(AssertionCondition condition, Duration timeout, String message) {
        try {
            AssertRetry.assertFalse(condition, timeout);
        } catch (AssertionTimeoutException e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Expected condition to be false.", e), e));
        }
    }

    /**
     * Verifies that the actual value equals the expected value.
     * This is a snapshot assertion and does not retry.
     * If the values are not equal, the failure is collected and
     * execution continues.
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
     * the expected value within the specified timeout.
     * The supplier is evaluated repeatedly until the assertion
     * passes or the timeout is reached.
     * If the values do not become equal within the timeout,
     * the failure is collected and execution continues.
     *
     * @param actualSupplier supplier that provides the actual value
     * @param expected       expected value
     * @param timeout        maximum time allowed for the assertion
     * @param message        custom assertion message
     * @param <T>            value type
     */
    public <T> void assertEquals(Supplier<T> actualSupplier, T expected, Duration timeout, String message) {
        try {
            AssertRetry.assertEquals(actualSupplier, expected, timeout);
        } catch (AssertionTimeoutException e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Expected: <" + expected + ">.", e), e));
        }
    }

    /**
     * Verifies that the actual value is different from the unexpected value.
     * This is a snapshot assertion and does not retry.
     * If the values are equal, the failure is collected and
     * execution continues.
     *
     * @param actual     actual value
     * @param unexpected value that the actual value must not equal
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
     * from the unexpected value within the specified timeout.
     * The supplier is evaluated repeatedly until the assertion
     * passes or the timeout is reached.
     * If the actual value remains equal to the unexpected value
     * until the timeout, the failure is collected and execution continues.
     *
     * @param actualSupplier supplier that provides the actual value
     * @param unexpected     value that the actual value must not equal
     * @param timeout        maximum time allowed for the assertion
     * @param message        custom assertion message
     * @param <T>            value type
     */
    public <T> void assertNotEquals(Supplier<T> actualSupplier, T unexpected, Duration timeout, String message) {
        try {
            AssertRetry.assertNotEquals(actualSupplier, unexpected, timeout);
        } catch (AssertionTimeoutException e) {
            Assertion.addFailure(new AssertionError(Assertion.buildMessage(message, "Values should not be equal: <" + unexpected + ">.", e), e));
        }
    }
}
