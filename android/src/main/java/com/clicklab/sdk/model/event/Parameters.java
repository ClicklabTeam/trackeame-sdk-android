package com.clicklab.sdk.model.event;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class Parameters {
    private Map<String, String> params = new HashMap<>();

    public Parameters set(@NonNull String key, String value) {
        if (value == null) {
            params.remove(key);
        } else if (value.length() > 0) {
            params.put(key, value);
        }
        return this;
    }

    public Parameters set(@NonNull String key, int value) {
        set(key, Integer.toString(value));
        return this;
    }


    public Parameters set(@NonNull String key, float value) {
        set(key, Float.toString(value));
        return this;
    }

    public Parameters set(@NonNull String key, long value) {
        set(key, Long.toString(value));
        return this;
    }

    public boolean isEmpty() {
        return params.isEmpty();
    }

    JSONObject getJsonObject() {
        return new JSONObject(params);
    }

}
