package com.clicklab.sdk.repository;

import com.clicklab.sdk.model.event.Event;

import java.util.LinkedList;
import java.util.List;

public class InMemoryEventRepository implements EventRepository {
    LinkedList<Event> eventList = new LinkedList<>();
    int count = 0;

    @Override
    public boolean isEmpty() {
        return eventList.isEmpty();
    }

    @Override
    public List<Event> getEvents() {
        LinkedList<Event> output = new LinkedList<>();
        for (Event event : eventList) {
            output.add(event);
        }
        return output;
    }

    @Override
    public void removeEvent(Event event) {
        for (Event dbEvent : eventList) {
            if (dbEvent.getId() == event.getId()) {
                eventList.remove(dbEvent);
                break;
            }
        }
    }

    @Override
    public void addEvent(String event) {
        eventList.add(new Event(count++, event));
    }

    @Override
    public void updateEvent(Event event) {
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getId() == event.getId()) {
                eventList.set(i, event);
                break;
            }
        }
    }
}
