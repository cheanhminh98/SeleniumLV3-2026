package com.assertion;

public class AssertionTimeoutException extends RuntimeException{

    /**
     * Creates an AssertionTimeoutException with the given message and cause.
     *
     * @param message the detail message explaining why the assertion timed out
     * @param cause   the underlying exception that caused this timeout
     */
    public AssertionTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
