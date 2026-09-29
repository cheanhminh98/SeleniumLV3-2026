package com.assertion;

@FunctionalInterface
public interface AssertionCondition {


    /**
     * Evaluates the assertion condition.
     *
     * @return true when the condition is satisfied
     */
    boolean evaluate();
}
