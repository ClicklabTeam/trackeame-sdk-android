package com.clicklab.sdk.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.clicklab.sdk.Configuration;
import com.clicklab.sdk.Constants;

public class AppInfo {

    private final String appPackage;
    private final String appName;

    public AppInfo(Context app, Configuration configuration) {
        this.appPackage = app.getPackageName();
        this.appName = configuration.getAppName();
    }

    /**
     * Gets the App package
     *
     * @return
     */
    public String getAppPackage() {
        return appPackage;
    }

    /**
     * Gets app name
     *
     * @return application name given in the configuration
     */
    public String getAppName() {
        return appName;
    }

    /**
     * Gets the App version
     *
     * @param context context where the app is running
     * @return String containing the app version
     */
    public String getAppVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            return manager.getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            Log.d(Constants.TAG, "Unnable to get app version");
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Gets the App installation Store
     *
     * @param context context where the app is running
     * @return String containing store name from where the app was installed
     */
    public String getAppStore(Context context) {
        String appStore = context.getPackageManager().getInstallerPackageName(context.getPackageName());
        return appStore != null ? appStore : "";
    }
}
