package com.assertion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Assertion {

    private static final HardAssertion HARD_ASSERTION = new HardAssertion();

    private static final ThreadLocal<SoftAssertion> SOFT_ASSERTION = ThreadLocal.withInitial(SoftAssertion::new);

    /**
     * Gets the hard assertion instance.
     *
     * @return hard assertion
     */
    public static HardAssertion hard() {
        return HARD_ASSERTION;
    }

    /**
     * Gets the soft assertion instance for the current thread.
     *
     * @return soft assertion
     */
    public static SoftAssertion soft() {
        return SOFT_ASSERTION.get();
    }

    /**
     * Verifies all soft assertions for the current test
     * and clears the current thread state.
     */
    public static void assertAll() {
        try {
            SOFT_ASSERTION.get().assertAll();
        } finally {
            SOFT_ASSERTION.remove();
        }
    }

    /**
     * Clears the soft assertion state for the current thread.
     */
    public static void clear() {
        SOFT_ASSERTION.remove();
    }

    /**
     * Builds the final assertion message.
     *
     * @param message        custom message
     * @param defaultMessage default assertion message
     * @return combined message
     */
    static String buildMessage(String message, String defaultMessage) {
        if (message == null || message.isBlank()) {
            return defaultMessage;
        }
        return message + System.lineSeparator() + defaultMessage;
    }
}
