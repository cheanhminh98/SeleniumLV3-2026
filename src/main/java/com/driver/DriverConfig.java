package com.driver;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DriverConfig {

    private String browser;
    private boolean headless;
    private String remoteURL;
    private String baseUrl;
    private boolean startMaximized;
    private long timeout;
    private long pageLoadTimeout;
    private long pollingInterval;

    /**
     * Checks whether the driver should run remotely.
     *
     * @return true if remote URL is configured
     */
    public boolean isRemote() {
        return remoteURL != null && !remoteURL.isEmpty();
    }
}
