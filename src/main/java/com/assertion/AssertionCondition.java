package com.assertion;

import java.time.Duration;
import java.util.Objects;

@FunctionalInterface
public interface AssertionCondition {

    /**
     * Evaluates the assertion condition.
     *
     * @return true when the condition is satisfied
     */
    boolean evaluate();

    /**
     * Evaluates the assertion condition with the supplied timeout.
     *
     * @return true when the condition is satisfied
     */
    default boolean evaluate(Duration timeout) {
        Objects.requireNonNull(timeout, "Timeout cannot be null.");
        return evaluate();
    }
}
