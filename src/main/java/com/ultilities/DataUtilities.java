package com.ultilities;

import com.constant.Constant;
import com.data.BrowserType;
import com.driver.DriverConfig;
import com.google.gson.JsonObject;

public class DataUtilities {

    private static JsonObject jsonObject;

    public DriverConfig getBrowserConfig(BrowserType browserType) {
        String configPath = Constant.BROWSER_CONFIG_PATH + browserType.name().toLowerCase() + ".json";
        return JsonHelper.getData(configPath, DriverConfig.class);
    }
}
