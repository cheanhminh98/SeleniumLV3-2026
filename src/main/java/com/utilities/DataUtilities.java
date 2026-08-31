package com.utilities;

import com.adapter.BrowserTypeAdapter;
import com.adapter.DurationTypeAdapter;
import com.constant.Constant;
import com.data.BrowserType;
import com.driver.DriverConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;

public class DataUtilities {

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
}
