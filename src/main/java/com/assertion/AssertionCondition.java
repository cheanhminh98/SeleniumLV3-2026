package com.assertion;

import java.time.Duration;

@FunctionalInterface
public interface AssertionCondition {

    /**
     * Evaluates the assertion condition.
     *
     * @return true when the condition is satisfied
     */
    boolean evaluate();

    /**
     * Evaluates the assertion condition.
     *
     * <p>The timeout parameter is intentionally ignored.
     * The assertion retry timeout is managed by {@link AssertRetry}.</p>
     *
     * @param timeout timeout supplied by the assertion retry mechanism
     * @return true when the condition is satisfied
     */
    default boolean evaluate(Duration timeout) {
        return evaluate();
    }
}
