package com.clicklab.sdk.model.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.clicklab.sdk.utils.JSonUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class BaseEvent {

    @Nullable
    protected Parameters extraParameters;

    static final String TRACKEAME_COOKIE = "trackeame_cookie";
    private static final String HASHED_EMAIL = "hm";
    private static final String COUNTRY = "country";
    private static final String API_KEY = "key";
    private static final String POST_ID = "post_id";
    static final String RETRY_COUNT = "post_retry_count";
    static final String DELAY = "delay";
    static final String TIMESTAMP = "timestamp";
    private static final String EXECUTION_ID = "execution_id";
    private static final String SDK_OS = "sdk_os";
    private static final String SDK_VERSION = "sdk_version";
    private static final String MOBILE_ID = "mobile_id";
    private static final String OS_BRAND = "os_brand";
    private static final String OS_VERSION = "os_version";
    private static final String APP_NAME = "app_name";
    private static final String APP_PACKAGE = "app_package";
    private static final String APP_VERSION = "app_version";
    private static final String APP_INSTALL_STORE = "app_install_store";
    private static final String HW_BRAND = "hw_brand";
    private static final String HW_VERSION = "hw_version";
    private static final String HW_TIMEZONE = "hw_timezone";
    private static final String HW_COUNTRY = "hw_country";
    private static final String HW_LANGUAGE = "hw_language";
    private static final String HW_CARRIER = "hw_carrier";
    private static final String HW_SCREEN_RES = "hw_screen_res";
    private static final String EXTRA_PARAMETERS = "extra_params";
    static final String CLICKLAB_EVENT_TYPE = "clicklab_event_type";

    private final HashMap<String, String> params = new HashMap<>();

    public void setTrackeameCookie(String trackeameCookie) {
        params.put(TRACKEAME_COOKIE, trackeameCookie);
    }

    public void setHashedEmail(String hashedEmail) {
        params.put(HASHED_EMAIL, hashedEmail);
    }

    public void setCountry(String country) {
        params.put(BaseEvent.COUNTRY, country);
    }

    public void setApiKey(String apiKey) {
        params.put(API_KEY, apiKey);
    }

    public void setPostId(String postId) {
        params.put(POST_ID, postId);
    }

    public void setRetryCount(int retryCount) {
        params.put(RETRY_COUNT, String.valueOf(retryCount));
    }

    protected void setEventType(EventType eventType) {
        set(CLICKLAB_EVENT_TYPE, eventType.name());
    }

    public void setExecutionId(String executionId) {
        params.put(EXECUTION_ID, executionId);
    }

    public void setSdkOs(String sdkOs) {
        params.put(SDK_OS, sdkOs);
    }

    public void setSdkVersion(String sdkVersion) {
        params.put(SDK_VERSION, sdkVersion);
    }

    public void setMobileId(String mobileId) {
        params.put(MOBILE_ID, mobileId);
    }

    public void setOsBrand(String osBrand) {
        params.put(OS_BRAND, osBrand);
    }

    public void setOsVersion(String osVersion) {
        params.put(OS_VERSION, osVersion);
    }

    public void setAppName(String appName) {
        params.put(APP_NAME, appName);
    }

    public void setAppPackage(String appPackage) {
        params.put(APP_PACKAGE, appPackage);
    }

    public void setAppVersion(String appVersion) {
        params.put(APP_VERSION, appVersion);
    }

    public void setAppInstallStore(String appInstallStore) {
        params.put(APP_INSTALL_STORE, appInstallStore);
    }

    public void setHwBrand(String hwBrand) {
        params.put(HW_BRAND, hwBrand);
    }

    public void setHwVersion(String hwVersion) {
        params.put(HW_VERSION, hwVersion);
    }

    public void setHwTimezone(String hwTimezone) {
        params.put(HW_TIMEZONE, hwTimezone);
    }

    public void setHwCountry(String hwCountry) {
        params.put(HW_COUNTRY, hwCountry);
    }

    public void setHwLanguage(String hwLanguage) {
        params.put(HW_LANGUAGE, hwLanguage);
    }

    public void setHwCarrier(String hwCarrier) {
        params.put(HW_CARRIER, hwCarrier);
    }

    public void setHwScreenRes(String screenRes) {
        params.put(HW_SCREEN_RES, screenRes);
    }

    BaseEvent() {
        set(TIMESTAMP, System.currentTimeMillis());
    }

    protected synchronized BaseEvent set(@NonNull String key, String value) {
        if (value == null) {
            params.remove(key);
        } else if (value.length() > 0) {
            params.put(key, value);
        }
        return this;
    }

    @Nullable
    public synchronized String get(@NonNull String key) {
        return params.get(key);
    }


    public BaseEvent set(@NonNull String key, int value) {
        set(key, Integer.toString(value));
        return this;
    }

    protected BaseEvent set(@NonNull String key, float value) {
        set(key, Float.toString(value));
        return this;
    }

    protected BaseEvent set(@NonNull String key, long value) {
        set(key, Long.toString(value));
        return this;
    }

    protected BaseEvent remove(@NonNull String key) {
        set(key, null);
        return this;
    }

    public boolean has(@NonNull String key) {
        return params.containsKey(key);
    }

    public synchronized boolean isEmpty() {
        return params.isEmpty();
    }

    @Override
    public String toString() {
        return getJsonObject().toString();
    }

    protected void setExtraParameters(@NonNull Parameters extraParameters) {
        this.extraParameters = extraParameters;
    }

    protected JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject(params);
        addExtraParameters(jsonObject);
        return jsonObject;
    }

    private void addExtraParameters(JSONObject jsonObject) {
        if (extraParameters != null && !extraParameters.isEmpty()) {
            JSonUtils.addElementToJsonObject(jsonObject, EXTRA_PARAMETERS, extraParameters.getJsonObject());
        }
    }
}