package com.data;

import com.driver.BaseDriver;
import com.driver.ChromeDriverManager;
import com.driver.EdgeDriverManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BrowserType {

    CHROME(ChromeDriverManager.class),
    EDGE(EdgeDriverManager .class);

    private final Class<? extends BaseDriver<?>> baseDriver;

    public static BrowserType getBrowser(String browser) {
        try {
            return valueOf(browser.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported browser: " + browser, e);
        }
    }
}
