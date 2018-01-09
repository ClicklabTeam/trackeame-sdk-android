package com.clicklab.sdk.model.event;

import android.support.annotation.NonNull;

public final class GenericEvent extends BaseEvent {

    private static final String CUSTOM_EVENT_TYPE = "custom_event_type";

    /**
     * Creates a new Generic Event
     * @param customEventType the type of the event that needs to be tracked
     */
    public GenericEvent(@NonNull String customEventType) {
        super();
        set(CUSTOM_EVENT_TYPE, customEventType);
        setEventType(EventType.generic);
    }

    /**
     * Creates a new Generic Event
     * @param customEventType The type of the event that needs to be tracked
     * @param extraParameters The extra parameters that need to be tracked
     */
    public GenericEvent(@NonNull String customEventType, @NonNull Parameters extraParameters) {
        super();
        set(CUSTOM_EVENT_TYPE, customEventType);
        setEventType(EventType.generic);
        setExtraParameters(extraParameters);
    }
}