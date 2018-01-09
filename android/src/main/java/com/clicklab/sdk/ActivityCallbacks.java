package com.clicklab.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.clicklab.sdk.model.event.OpenAppEvent;
import com.clicklab.sdk.utils.Preconditions;

import static com.clicklab.sdk.ActivityCallbacks.State.NULL;
import static com.clicklab.sdk.ActivityCallbacks.State.PAUSE;
import static com.clicklab.sdk.ActivityCallbacks.State.RESUME;

public class ActivityCallbacks implements Application.ActivityLifecycleCallbacks {

    enum State {RESUME, PAUSE, NULL}

    private static final int BACKGROUND_THERESHOLD = 3000;

    private State state = NULL;
    private long timestamp = 0;
    private boolean home = false;
    private String deepLink;
    private ClicklabClient clientInstance = null;

    /**
     * Allows tracking for states changes in activity lifecycle
     *
     * @param clientInstance ClickLabClient instance for tracking
     */
    ActivityCallbacks(@NonNull ClicklabClient clientInstance) {
        Preconditions.checkNotNull(clientInstance);
        this.clientInstance = clientInstance;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity.getIntent().getData() != null) {
            deepLink = activity.getIntent().getData().toString();
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
        state = PAUSE;
        timestamp = System.currentTimeMillis();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (clientInstance == null) {
            return;
        }
        boolean background = wasInBackground();
        state = RESUME;

        if (background) {
            clientInstance.logEvent(new OpenAppEvent(deepLink, ""));
        }
        clearFlags();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outstate) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    private void clearFlags() {
        home = false;
        timestamp = getCurrentTimeMillis();
        deepLink = "";
    }

    private long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    private boolean wasInBackground() {
        return (NULL.equals(state) || PAUSE.equals(state)) &&
                (System.currentTimeMillis() - timestamp > BACKGROUND_THERESHOLD);
    }
}
