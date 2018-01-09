package com.clicklab.sdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.clicklab.sdk.model.SendEventsAction;
import com.clicklab.sdk.model.event.BaseEvent;
import com.clicklab.sdk.model.event.ConversionEvent;
import com.clicklab.sdk.model.event.GenericEvent;
import com.clicklab.sdk.model.event.InstallEvent;
import com.clicklab.sdk.model.event.OpenAppEvent;
import com.clicklab.sdk.repository.DefaultDispatcher;
import com.clicklab.sdk.repository.Dispatcher;
import com.clicklab.sdk.repository.EventRepository;
import com.clicklab.sdk.repository.EventRepositoryFactory;
import com.clicklab.sdk.repository.OkHttpClientFactory;
import com.clicklab.sdk.repository.SdkRepository;
import com.clicklab.sdk.repository.SdkRepositoryFactory;
import com.clicklab.sdk.utils.AppInfo;
import com.clicklab.sdk.utils.DeviceInfo;
import com.clicklab.sdk.utils.Hasher;
import com.clicklab.sdk.utils.SdkInfo;

import java.util.Locale;

public class ClicklabClient {
    private final SdkRepository sdkRepository;
    private final SendEventsAction sendEventAction;
    private UserData userData;
    private Configuration configuration;
    private Dispatcher dispatcher;
    private EventRepository eventRepository;

    /**
     * Constructor for clicklab Client
     *
     * @param context       Context
     * @param configuration Tracker configuration
     */
    ClicklabClient(Context context, Configuration configuration) {
        this.configuration = configuration;
        this.sdkRepository = new SdkRepositoryFactory().getSdkRepository(context);
        configureUserData(context, configuration, sdkRepository);
        this.eventRepository = EventRepositoryFactory.getEventRepository(context);
        this.dispatcher = new DefaultDispatcher(new OkHttpClientFactory().getClient());
        this.sendEventAction = new SendEventsAction(eventRepository, sdkRepository, dispatcher, context);
        trackAppInstall();
    }

    /**
     * Sets the country of the current user
     *
     * @param locale Locale of the country
     * @return this client
     */
    public ClicklabClient setCountry(Locale locale) {
        Log.d(Constants.TAG, "Country set: " + locale.getCountry());
        configuration.setLocaleCountry(locale);
        userData.setCountry(locale.getCountry());
        return this;
    }

    /**
     * Sets the email of the current user
     *
     * @param email current email
     * @return this client
     */
    public ClicklabClient setEmail(@NonNull String email) {
        Log.d(Constants.TAG, "Email set: " + email);
        userData.setEmail(new Hasher().SHA1Encode(email));
        return this;
    }

    /**
     * Logs the OpenAppEvent that is provided
     * It saves the event and try to send it as soon as possible
     *
     * @param event OpenAppEvent event to be tracked
     */
    public void logEvent(OpenAppEvent event) {
        log(event);
    }

    /**
     * Logs the ConversionEvent that is provided
     * It saves the event and try to send it as soon as possible
     *
     * @param event ConversionEvent event to be tracked
     */
    public void logEvent(ConversionEvent event) {
        log(event);
    }

    /**
     * Logs the GenericEvent that is provided
     * It saves the event and try to send it as soon as possible
     *
     * @param event GenericEvent event to be tracked
     */
    public void logEvent(GenericEvent event) {
        log(event);
    }

    void syncEvents() {
        sendEventAction.execute();
    }

    interface SyncEventsCallback {
        void finished();
    }

    void syncEvents(final SyncEventsCallback callback) {
        sendEventAction.execute(new SendEventsAction.Callback() {
            @Override
            public void finished() {
                callback.finished();
            }
        });
    }

    private void log(BaseEvent event) {
        Log.d(Constants.TAG, "Event recieved for tracking: " + event.getClass().getSimpleName() + " " + event.toString());
        addEventToRepository(event);
        syncEvents();
    }

    private void trackAppInstall() {
        if (!sdkRepository.getAppInstallTracked()) {
            sdkRepository.setAppInstallTracked();
            this.log(new InstallEvent(sdkRepository.getInstallReferrer()));
        }
    }

    private void configureUserData(Context context, Configuration configuration, SdkRepository sdkRepository) {
        this.userData = new UserData(context.getApplicationContext(), new AppInfo(context, configuration), new DeviceInfo(), new SdkInfo(sdkRepository, configuration));
        userData.setEmail(configuration.getEmail());
        userData.setCountry(configuration.getCountry());
    }

    private void addEventToRepository(BaseEvent event) {
        userData.fillEventParameters(event);
        eventRepository.addEvent(event.toString());
    }
}
