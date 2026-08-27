package com.driver;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DriverConfig {

    private String remoteURL;
    private boolean headless;

    /**
     * Checks whether the driver should run remotely.
     *
     * @return true if remote URL is configured
     */
    public boolean isRemote() {
        return remoteURL != null && !remoteURL.isEmpty();
    }
}
