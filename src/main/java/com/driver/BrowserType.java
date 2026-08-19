package com.driver;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BrowserType {

    CHROME("Chrome", ChromeDriverManager.class);

    private final String browserName;
    private final Class<? extends BaseDriver<?>> baseDriver;
}
