package com.clicklab.sdk.repository;

import com.clicklab.sdk.model.event.Event;

import java.util.List;

public interface EventRepository {
    boolean isEmpty();

    List<Event> getEvents();

    void removeEvent(Event event);

    void addEvent(String event);

    void updateEvent(Event event);
}
