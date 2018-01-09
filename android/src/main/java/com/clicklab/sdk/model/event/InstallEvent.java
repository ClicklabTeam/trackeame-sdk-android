package com.clicklab.sdk.model.event;

public final class InstallEvent extends BaseEvent {
    private static final String REFERRER = "referrer";

    /**
     * Creates a new Installation event
     * @param referrer referrer of the installation
     */
    public InstallEvent(String referrer) {
        set(REFERRER, referrer);
        setEventType(EventType.install);
    }
}
