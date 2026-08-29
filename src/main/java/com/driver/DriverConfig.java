package com.driver;

import com.data.BrowserType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

@Getter
@AllArgsConstructor
public class DriverConfig {

    private BrowserType browser;
    private boolean headless;
    private String remoteURL;
    private String baseUrl;
    private boolean startMaximized;
    private Duration timeout;
    private Duration pageLoadTimeout;
    private Duration pollingInterval;

    /**
     * Creates DriverConfig with default values.
     */
    public DriverConfig() {
        browser = BrowserType.CHROME;
        headless = false;
        remoteURL = "";
        baseUrl = "https://google.com";
        startMaximized = true;
        timeout = Duration.ofSeconds(5);
        pageLoadTimeout = Duration.ofSeconds(30);
        pollingInterval = Duration.ofMillis(200);
    }

    /**
     * Checks whether the driver should run remotely.
     *
     * @return true if remote URL is configured
     */
    public boolean isRemote() {
        return remoteURL != null && !remoteURL.isEmpty();
    }
}
