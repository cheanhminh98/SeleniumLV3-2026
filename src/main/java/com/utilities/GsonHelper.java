package com.utilities;

import com.adapter.DurationTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;

public class GsonHelper {

    /**
     * Gets Gson configured for driver configuration.
     *
     * @return configured Gson instance
     */
    public static Gson getGsonForDriver() {
        return new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
    }
}
