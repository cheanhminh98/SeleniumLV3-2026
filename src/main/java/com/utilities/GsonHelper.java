package com.utilities;

import com.adapter.BrowserTypeAdapter;
import com.adapter.DurationTypeAdapter;
import com.data.BrowserType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;

public class GsonHelper {

    /**
     * Get Gson for driver configuration.
     *
     * @return configured Gson instance
     */
    public static Gson getGsonForDriver() {
        return new GsonBuilder()
                .registerTypeAdapter(BrowserType.class, new BrowserTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
    }
}
