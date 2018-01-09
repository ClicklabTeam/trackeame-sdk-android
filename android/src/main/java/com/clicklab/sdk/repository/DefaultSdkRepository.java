package com.clicklab.sdk.repository;


import android.content.Context;
import android.content.SharedPreferences;

import com.clicklab.sdk.utils.Utils;

public class DefaultSdkRepository implements SdkRepository {

    private static final String SDK_PREF = "cookie_preference";
    private static final String COOKIE_KEY = "cookie_key";
    private static final String REFERRER_KEY = "referrer_key";
    private static final String APP_INSTALLED_KEY = "app_installed_key";

    private SharedPreferences sharedPref;
    private String cookie = "";

    DefaultSdkRepository(Context context) {
        sharedPref = context.getSharedPreferences(SDK_PREF, Context.MODE_PRIVATE);
    }

    @Override
    public synchronized String getCookie() {
        return Utils.isEmptyString(cookie) ? cookie = sharedPref.getString(COOKIE_KEY, "") : cookie;
    }

    @Override
    public synchronized void setCookie(String cookie) {
        if (Utils.isEmptyString(cookie) || this.cookie.equals(cookie)) {
            return;
        }
        this.cookie = cookie;
        sharedPref.edit().putString(COOKIE_KEY, cookie).apply();
    }

    @Override
    public void setInstallReferrer(String referrer) {
        sharedPref.edit().putString(REFERRER_KEY, referrer).commit();
    }

    @Override
    public String getInstallReferrer() {
        return sharedPref.getString(REFERRER_KEY, "");
    }

    @Override
    public void setAppInstallTracked() {
        sharedPref.edit().putBoolean(APP_INSTALLED_KEY, true).apply();
    }

    @Override
    public boolean getAppInstallTracked() {
        return sharedPref.getBoolean(APP_INSTALLED_KEY, false);
    }
}
