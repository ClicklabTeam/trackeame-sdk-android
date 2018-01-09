package com.clicklab.sdk.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.clicklab.sdk.Constants;
import com.clicklab.sdk.model.NetworkErrorSendException;
import com.clicklab.sdk.model.event.EventType;
import com.clicklab.sdk.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DefaultDispatcher implements Dispatcher {
    private OkHttpClient client;
    private Callback callback;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String COOKIE_PARAMETER = "trackeame_cookie";

    public DefaultDispatcher(@NonNull OkHttpClient client) {
        this.client = client;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void sendOneEvent(@NonNull String event, @NonNull EventType eventType) throws NetworkErrorSendException {
        Request request = buildRequest(event, eventType);
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                updateCookie(response);
                Log.d(Constants.TAG, "Event sent: " + event);
            } else {
                Log.d(Constants.TAG, "Event NOT sent - error code " +
                        response.code() + " " + event + getResponseBody(response));
            }
        } catch (IOException e) {
            Log.d(Constants.TAG, "Event NOT sent - Network Exception " + e.toString() +
                    " event: " + event);
            throw new NetworkErrorSendException();
        }
    }


    private Request buildRequest(String eventString, EventType eventType) {
        RequestBody body = RequestBody.create(JSON, eventString);
        return new Request.Builder()
                .url(Constants.API_URL_BASE + eventType.getEndpoint())
                .post(body)
                .build();
    }

    private void updateCookie(Response response) {
        String responseBody = getResponseBody(response);
        Log.v(Constants.TAG, "response " + responseBody);
        informCookieRecieved(responseBody);
    }

    private String getResponseBody(Response response) {
        String responseBody = "";
        try {
            if (response.body() == null) {
                response.close();
            } else {
                responseBody = response.body().string();
            }
        } catch (IOException ex) {
            System.out.println("IOException");
        }
        return responseBody;
    }

    private void informCookieRecieved(String responseBody) {
        try {
            if (!Utils.isEmptyString(responseBody)) {
                JSONObject jsonObject = new JSONObject(responseBody);
                if (jsonObject.has(COOKIE_PARAMETER)) {
                    callback.cookieRecieved(jsonObject.getString(COOKIE_PARAMETER));
                }
            }
        } catch (JSONException e) {
            System.out.println("json exception");
        }
    }
}
