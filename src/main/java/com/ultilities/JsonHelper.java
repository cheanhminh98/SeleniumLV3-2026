package com.ultilities;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;

@Slf4j
public class JsonHelper {

    public static <T> T getData(String jsonFile, Class<?> clazz) {
        try {
            log.debug("JsonHelper: getData");
            Gson gson = new Gson();
            JsonReader reader = getJsonReader(jsonFile);
            return gson.fromJson(reader, clazz);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(jsonFile + "does not exist");
        }
    }

    private static JsonReader getJsonReader(String jsonFile) {
        try {
            JsonReader reader;
            reader = new JsonReader(new FileReader(jsonFile));
            return reader;
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
