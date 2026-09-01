package com.adapter;

import com.data.BrowserType;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class BrowserTypeAdapter extends TypeAdapter<BrowserType> {

    /**
     * Writes a BrowserType value to JSON.
     *
     * @param out JSON writer
     * @param value browser type
     */
    @Override
    public void write(JsonWriter out, BrowserType value) throws IOException {
        if (value == null) {
            out.nullValue();
        }
        else {
            out.value(value.name());
        }
    }

    /**
     * Reads a BrowserType value from JSON.
     *
     * @param in JSON reader
     * @return browser type
     */
    @Override
    public BrowserType read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return BrowserType.getBrowser(in.nextString());
    }
}
