package com.utilities;

import com.constant.Constant;
import com.data.BrowserType;
import com.driver.DriverConfig;

import java.util.Objects;

public class DriverConfigLoader {

    /**
     * Gets driver configuration for the specified browser.
     *
     * @param browserType browser type
     * @return resolved driver configuration
     */
    public DriverConfig getDriverConfig(BrowserType browserType) {
        Objects.requireNonNull(browserType, "BrowserType cannot be null.");

        String filePath = Constant.CONFIG_PATH + browserType.name().toLowerCase() + ".json";

        DriverConfig config = Objects.requireNonNull(JsonHelper.getData(filePath, DriverConfig.class, GsonHelper.getGsonForDriver()), "Driver configuration cannot be null.");

        applySystemProperties(config);

        return config;
    }

    /**
     * Overrides JSON values when the corresponding system property is set.
     *
     * @param config driver configuration
     */
    private void applySystemProperties(DriverConfig config) {
        String headless = System.getProperty("headless");
        if (headless != null) {
            if (!headless.equalsIgnoreCase("true") && !headless.equalsIgnoreCase("false")) {
                throw new IllegalArgumentException("System property 'headless' must be true or false.");
            }
            config.setHeadless(Boolean.parseBoolean(headless));
        }

        String remoteURL = System.getProperty("remoteURL");
        if (remoteURL != null) {
            config.setRemoteURL(remoteURL.trim());
        }

        String baseUrl = System.getProperty("baseUrl");
        if (baseUrl != null) {
            config.setBaseUrl(baseUrl.trim());
        }
    }
}
