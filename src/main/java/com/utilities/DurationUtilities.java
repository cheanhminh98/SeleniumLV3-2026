package com.utilities;

import java.time.Duration;

public class DurationUtilities {

    /**
     * Creates a Duration from milliseconds.
     */
    public static Duration ofMillis(long milliseconds) {
        return Duration.ofMillis(milliseconds);
    }

    /**
     * Creates a Duration from seconds.
     */
    public static Duration ofSeconds(long seconds) {
        return Duration.ofSeconds(seconds);
    }

    /**
     * Creates a Duration from minutes.
     */
    public static Duration ofMinutes(long minutes) {
        return Duration.ofMinutes(minutes);
    }

    /**
     * Creates a Duration from hours.
     */
    public static Duration ofHours(long hours) {
        return Duration.ofHours(hours);
    }
}
