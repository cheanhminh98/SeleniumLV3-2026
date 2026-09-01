package com.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationTypeAdapter extends TypeAdapter<Duration> {

    /**
     * Writes a Duration value to JSON as milliseconds.
     *
     * @param out JSON writer
     * @param value duration value
     */
    @Override
    public void write(JsonWriter out, Duration value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toMillis());
        }
    }

    /**
     * Reads a Duration value from JSON milliseconds.
     *
     * @param in JSON reader
     * @return duration value
     */
    @Override
    public Duration read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return Duration.ofMillis(in.nextLong());
    }
}
