package com.utilities;

import com.data.BrowserType;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.Duration;

@Slf4j
public class JsonHelper {

    /**
     * Gets data from a JSON file.
     *
     * @param jsonPath JSON file path
     * @param clazz target class
     * @param <T> target type
     * @return parsed data
     */
    public static <T> T getData(String jsonPath, Class<T> clazz) {
        try {
            log.debug("JsonHelper: getData");
            JsonReader reader = getJsonReader(jsonPath);
            return GSON.fromJson(reader, clazz);
        } catch (JsonSyntaxException e) {
            log.error("Failed to read JSON: {}", jsonPath, e);
            throw new RuntimeException("Cannot read JSON file: " + jsonPath, e);
        }
    }

    /**
     * Gets a JSON reader from the given file path.
     *
     * @param jsonPath JSON file path
     * @return JSON reader
     */
    private static JsonReader getJsonReader(String jsonPath) {
        try {
            return new JsonReader(new FileReader(jsonPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("JSON file does not exist: " + jsonPath, e);
        }
    }

    /**
     * Gson instance with custom deserializers.
     */
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(BrowserType.class, (JsonDeserializer<BrowserType>)
                    (json, type, context) -> BrowserType.getBrowser(json.getAsString()))
            .registerTypeAdapter(Duration.class,
                    (JsonDeserializer<Duration>) (json, type, context) -> DurationUtilities.ofMillis(json.getAsLong()))
            .create();
}

