package com.driver;

import com.data.BrowserType;

public class BaseDriverFactory {

    /**
     * Gets the driver manager for the specified browser.
     *
     * @param browserType browser type
     * @return driver
     */
    public static BaseDriver<?> getDriver(BrowserType browserType) {
        try {
            return browserType
                    .getBaseDriver()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Cannot create driver for browser: " + browserType, e);
        }
    }
}
