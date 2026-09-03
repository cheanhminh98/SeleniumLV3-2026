package com.utilities;

import com.constant.Constant;
import com.data.BrowserType;
import com.driver.DriverConfig;
import com.google.gson.JsonObject;

public class DriverConfigLoader {

    /**
     * Gets driver configuration for the specified browser.
     *
     * @param browserType browser type
     * @return driver configuration
     */
    public DriverConfig getDriverConfig(BrowserType browserType) {
        String filePath = Constant.CONFIG_PATH + browserType.name().toLowerCase() + ".json";
        return JsonHelper.getData(filePath, DriverConfig.class, GsonHelper.getGsonForDriver());
    }

    /**
     * Gets the browser name.
     *
     * @param browserType browser type
     * @return browser name
     */

    public String getBrowserName(BrowserType browserType) {
        return getDriverConfig(browserType)
                .getBrowser()
                .name()
                .toLowerCase();
    }
}
