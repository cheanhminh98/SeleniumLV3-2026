package com.driver;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BrowserType {

    CHROME("Chrome", ChromeDriverManager.class);

    private final String browserName;
    private final Class<? extends BaseDriver<?>> baseDriver;

    public static BrowserType getBrowser(String browser) {
        try {
            return valueOf(browser.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported browser: " + browser, e);
        }
    }
}
