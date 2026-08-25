package com.driver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DriverConfig {

    private String browser;
    private String remoteURL;
    private String baseUrl;
    private boolean headless;
    private boolean startMaximized;
    private long timeout;
    private long pageLoadTimeout;
    private long pollingInterval;
    private List<String> arguments;

    /**
     * Checks whether the driver should run remotely.
     *
     * @return true if remote URL is configured
     */
    public boolean isRemote() {
        return remoteURL != null && !remoteURL.isEmpty();
    }
}
