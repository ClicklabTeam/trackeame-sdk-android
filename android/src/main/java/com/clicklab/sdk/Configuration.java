package com.clicklab.sdk;

import android.support.annotation.NonNull;

import com.clicklab.sdk.utils.EmailValidator;
import com.clicklab.sdk.utils.Preconditions;
import com.clicklab.sdk.utils.Utils;

import java.util.Locale;

public class Configuration {
    private String email = "";
    private String localeCountry = "";
    private String apiKey;
    private String appName;

    public Configuration(@NonNull String apiKey, @NonNull String appName) {
        checkPreconditions(apiKey, appName);

        this.apiKey = apiKey;
        this.appName = appName;
    }

    public Configuration setEmail(@NonNull String email) {
        Preconditions.checkNotNull(email);
        Preconditions.checkTrue(Utils.isEmptyString(email) || new EmailValidator().validate(email));
        this.email = email;
        return this;
    }

    public Configuration setLocaleCountry(@NonNull Locale localeCountry) {
        Preconditions.checkNotNull(localeCountry);
        this.localeCountry = localeCountry.getCountry();
        return this;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return localeCountry;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getAppName() {
        return appName;
    }

    private void checkPreconditions(@NonNull String apiKey, @NonNull String appName) {
        Preconditions.checkNotNull(apiKey);
        Preconditions.checkNotNull(appName);
        Preconditions.checkTrue(!Utils.isEmptyString(apiKey));
        Preconditions.checkTrue(!Utils.isEmptyString(appName));
    }
}
