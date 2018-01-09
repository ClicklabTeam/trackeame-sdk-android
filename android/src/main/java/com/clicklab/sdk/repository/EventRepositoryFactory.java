package com.clicklab.sdk.repository;

import android.content.Context;

public class EventRepositoryFactory {
    private static EventRepository eventRepository;

    public static EventRepository getEventRepository(Context context) {
        if (eventRepository == null) {
            eventRepository = new DbEventRepository(context);
        }
        return eventRepository;
    }
}
