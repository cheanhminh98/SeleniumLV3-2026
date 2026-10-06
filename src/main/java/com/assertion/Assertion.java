package com.assertion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Assertion {

    private static final ThreadLocal<List<AssertionError>> FAILURES = ThreadLocal.withInitial(ArrayList::new);

    /**
     * Adds an assertion failure to the current test thread.
     *
     * @param failure assertion failure
     */
    static void addFailure(AssertionError failure) {
        FAILURES.get().add(failure);
    }

    /**
     * Verifies all collected assertion failures.
     *
     * @throws AssertionError when one or more assertions failed
     */
    public static void assertAll() {
        List<AssertionError> failures = FAILURES.get();
        if (failures.isEmpty()) {
            return;
        }
        String message = failures.stream().map(AssertionError::getMessage)
                .collect(Collectors.joining(System.lineSeparator(),
                        "The following assertions failed:"
                                + System.lineSeparator(), ""));
        AssertionError error = new AssertionError(message);
        failures.forEach(error::addSuppressed);
        throw error;
    }

    /**
     * Verifies all assertions and clears the current test state.
     *
     */
    public static void finishTest() {
        assertAll();
    }

    /**
     * Clears all assertion failures for the current test thread.
     *
     */
    public static void clear() {
        FAILURES.remove();
    }

    /**
     * Builds an assertion message.
     *
     * @param message        custom assertion message
     * @param defaultMessage default assertion details
     * @return formatted assertion message
     */
    static String buildMessage(String message, String defaultMessage) {
        if (message == null || message.isBlank()) {
            return defaultMessage;
        }
        return message + System.lineSeparator() + defaultMessage;
    }

    /**
     * Builds an assertion message with the original cause.
     *
     * @param message        custom assertion message
     * @param defaultMessage default assertion details
     * @param cause          original exception
     * @return formatted assertion message
     */
    static String buildMessage(String message, String defaultMessage, Throwable cause) {
        String assertionMessage = buildMessage(message, defaultMessage);
        if (cause == null || cause.getMessage() == null) {
            return assertionMessage;
        }
        return assertionMessage + System.lineSeparator() + "Cause: " + cause.getMessage();
    }
}
