package com.clicklab.sdk;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.clicklab.sdk.utils.Preconditions;

public class ClickLab {

    private static ClicklabClient client;

    /**
     * The SDK needs to be initialized in the Application class using this method
     *
     * @param app           Android Application
     * @param configuration Configuration to the sdk
     */
    public static void init(@NonNull Application app, @NonNull Configuration configuration) {
        client = new ClicklabClient(app, configuration);
        app.registerActivityLifecycleCallbacks(new ActivityCallbacks(client));
        Log.d(Constants.TAG, "Clicklab successfully initialized ");
    }

    /**
     * Provides an instance of ClicklabClient to perform different operations
     * like tracking events or changing runtime parameters
     *
     * @return ClicklabClient
     */
    public static ClicklabClient get() {
        Preconditions.checkNotNull(client, "Clicklab has not been initialized");
        return client;
    }

}
