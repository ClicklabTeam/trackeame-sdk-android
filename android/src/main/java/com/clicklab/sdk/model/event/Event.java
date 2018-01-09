package com.clicklab.sdk.model.event;


import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Event {
    private long id;
    private String eventString;
    private JSONObject eventJson;

    public Event(long id, String event) {
        this.id = id;
        this.eventString = event;
    }

    public long getId() {
        return id;
    }

    public String getEventString() {
        return eventJson != null ? eventJson.toString() : eventString;
    }

    public EventType getEventType() {
        EventType eventType;
        try {
            eventType = EventType.valueOf((String) getEventJson().get(BaseEvent.CLICKLAB_EVENT_TYPE));
        } catch (JSONException e) {
            eventType = EventType.generic;
        }
        return eventType;
    }

    private JSONObject getEventJson() {
        prepareEventJson();
        return eventJson;
    }

    private void prepareEventJson() {
        if (this.eventJson != null) {
            return;
        }
        try {
            this.eventJson = new JSONObject(eventString);
        } catch (JSONException e) {
            e.printStackTrace();
            this.eventJson = new JSONObject();
        }
    }

    public void addRetryCount() {
        prepareEventJson();
        try {
            int count = getEventJson().getInt(BaseEvent.RETRY_COUNT);
            getEventJson().put(BaseEvent.RETRY_COUNT, ++count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDataBeforeSend(@NonNull String cookie) {
        setDelay();
        setCookie(cookie);
    }

    private void setCookie(String cookie) {
        try {
            getEventJson().put(BaseEvent.TRACKEAME_COOKIE, cookie);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setDelay() {
        try {
            long timestamp = getEventJson().getLong(BaseEvent.TIMESTAMP);
            int delay = (int) (System.currentTimeMillis() - timestamp) / 1000;
            getEventJson().put(BaseEvent.DELAY, delay);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
