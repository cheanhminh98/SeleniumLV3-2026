package com.element;

import java.util.Objects;

@FunctionalInterface
public interface ElementCondition {

    /**
     * Checks whether the condition is satisfied.
     *
     * @param element element to evaluate
     * @return true if the condition is satisfied
     */
    boolean matches(Element element);

    /**
     * Combines this condition with another condition.
     *
     * @param other condition to combine with
     * @return combined condition that requires both conditions
     *         to be satisfied
     */
    default ElementCondition and(ElementCondition other) {
        Objects.requireNonNull(other, "ElementCondition cannot be null.");
        return element -> matches(element) && other.matches(element);
    }

    /**
     * Combines this condition with another condition.
     *
     * @param other condition to combine with
     * @return combined condition that requires at least one condition
     *         to be satisfied
     */
    default ElementCondition or(ElementCondition other) {
        Objects.requireNonNull(other, "ElementCondition cannot be null.");
        return element -> matches(element) || other.matches(element);
    }
}
