package com.clicklab.sdk.repository;

import com.clicklab.sdk.model.NetworkErrorSendException;
import com.clicklab.sdk.model.event.EventType;

public interface Dispatcher {
    void sendOneEvent(String event, EventType eventType) throws NetworkErrorSendException;

    void setCallback(Callback callback);

    interface Callback {
        void cookieRecieved(String cookie);
    }
}
