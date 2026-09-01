package com.data;

import com.driver.BaseDriver;
import com.driver.ChromeDriverManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BrowserType {

    CHROME(ChromeDriverManager.class);

    private final Class<? extends BaseDriver<?>> baseDriver;

    /**
     * Gets the browser type
     *
     * @param browser browser name
     * @return browser type
     */
    public static BrowserType getBrowser(String browser) {
        if (browser == null || browser.isBlank()) {
            throw new IllegalArgumentException("Browser cannot be null or empty");
        }
        try {
            return valueOf(browser.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported browser: " + browser, e);
        }
    }
}
