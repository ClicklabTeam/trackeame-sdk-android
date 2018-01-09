package com.clicklab.sdk.model.event;

public final class OpenAppEvent extends BaseEvent {
    private static final String URL = "url";
    private static final String REFERRER = "referrer";

    /**
     * Creates a new OpenAppEvent
     * This event is tracked automatically but it could also be tracked manually
     *
     * @param url      URL that generated the app opening
     * @param referrer Referrer of the opening
     */
    public OpenAppEvent(String url, String referrer) {
        setEventType(EventType.openapp);
        set(URL, url);
        set(REFERRER, referrer);
    }

    /**
     * Creates a new OpenAppEvent
     * This event is tracked automatically but it could also be tracked manually
     *
     * @param url             URL that generated the app opening
     * @param referrer        Referrer of the opening
     * @param extraParameters The extra parameters that need to be tracked
     */
    public OpenAppEvent(String url, String referrer, Parameters extraParameters) {
        this.extraParameters = extraParameters;
        setEventType(EventType.openapp);
        set(URL, url);
        set(REFERRER, referrer);
        setExtraParameters(extraParameters);
    }
}
