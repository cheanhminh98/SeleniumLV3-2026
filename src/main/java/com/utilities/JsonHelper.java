package com.utilities;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;

@Slf4j
public class JsonHelper {

    public static <T> T getData(String jsonPath, Class<?> clazz) {
        try {
            log.debug("JsonHelper: getData");
            Gson gson = new Gson();
            JsonReader reader = getJsonReader(jsonPath);
            return gson.fromJson(reader, clazz);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private static JsonReader getJsonReader(String jsonPath) {
        try {
            JsonReader reader;
            reader = new JsonReader(new FileReader(jsonPath));
            return reader;
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
