package com.clicklab.sdk.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.clicklab.sdk.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class JSonUtils {
    public static JSONObject addElementToJsonObject(@NonNull JSONObject jsonObject, @NonNull String childName, @NonNull JSONObject childObject) {
        try {
            jsonObject.put(childName, childObject);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(Constants.TAG, "Unnable to addElement to JsonObject");
        }
        return jsonObject;
    }

    public static JSONObject addArrayToJsonObject(@NonNull JSONObject jsonObject, @NonNull String childName, @NonNull JSONArray childObject) {
        try {
            jsonObject.put(childName, childObject);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(Constants.TAG, "Unnable to add Array to JsonObject");
        }
        return jsonObject;
    }

    public static JSONObject addStringMapToJsonObjectIfIsNotEmpty(@NonNull JSONObject jsonObject, @NonNull String childName, @NonNull Map<String, String> map) {
        if (!map.isEmpty()) {
            jsonObject = addElementToJsonObject(jsonObject, childName, new JSONObject(map));
        }
        return jsonObject;
    }
}
