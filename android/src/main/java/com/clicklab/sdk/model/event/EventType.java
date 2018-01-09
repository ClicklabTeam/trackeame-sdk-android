package com.clicklab.sdk.model.event;

import com.clicklab.sdk.Constants;

public enum EventType {
    generic(Constants.API_EVENT),
    install(Constants.API_EVENT),
    openapp(Constants.API_EVENT),
    conversion(Constants.API_CONVERSION);

    private final String endpoint;

    EventType(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
