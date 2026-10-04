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
}
