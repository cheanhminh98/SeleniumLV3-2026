package com.utilities;

import com.constant.Constant;
import com.driver.BrowserType;
import com.driver.DriverConfig;

public class DataUtilities {
    public DriverConfig getDriverConfig(BrowserType browserType) {
        String filePath = Constant.CONFIG_PATH + browserType.name().toLowerCase() + ".json";
        return JsonHelper.getData(filePath, DriverConfig.class
        );
    }
}
