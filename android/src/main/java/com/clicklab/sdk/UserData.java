package com.clicklab.sdk;

import android.content.Context;
import android.support.annotation.NonNull;

import com.clicklab.sdk.model.event.BaseEvent;
import com.clicklab.sdk.utils.AppInfo;
import com.clicklab.sdk.utils.DeviceInfo;
import com.clicklab.sdk.utils.Hasher;
import com.clicklab.sdk.utils.SdkInfo;
import com.clicklab.sdk.utils.Utils;

import java.util.Locale;
import java.util.UUID;

class UserData {

    private static final String TRAILING_ZEROS = "%07d";

    private String cookie = "";
    private String hashedEmail;
    private String country = "";
    private String apiKey = "";
    private String sdkOs = "";
    private String sdkVersion = "";
    private String osBrand = "";
    private String osVersion = "";
    private String mobileId = "";
    private String appName = "";
    private String appPackage = "";
    private String appVersion = "";
    private String appStore = "";
    private String hwBrand = "";
    private String hwVersion = "";
    private String hwTimezone = "";
    private String hwCountry = "";
    private String hwLanguage = "";
    private String hwCarrier = "";
    private String hwScreenSize = "";
    private int eventCounter = 0;
    private String eventHash = "";

    public UserData(Context context, AppInfo appInfo, DeviceInfo deviceInfo, SdkInfo sdkInfo) {
        setAppParameters(context, appInfo);
        setDeviceParameters(context, deviceInfo);
        setSdkParameters(sdkInfo);
    }

    void fillEventParameters(@NonNull BaseEvent baseEvent) {
        baseEvent.setTrackeameCookie(cookie);
        baseEvent.setHashedEmail(hashedEmail);
        baseEvent.setCountry(country);
        baseEvent.setApiKey(apiKey);
        eventCounter++;
        baseEvent.setPostId(getPostId());
        baseEvent.setRetryCount(0);
        baseEvent.setExecutionId(eventHash); // check
        baseEvent.setSdkOs(sdkOs);
        baseEvent.setSdkVersion(sdkVersion);
        baseEvent.setMobileId(mobileId);
        baseEvent.setOsBrand(osBrand);
        baseEvent.setOsVersion(osVersion);
        baseEvent.setAppName(appName);
        baseEvent.setAppPackage(appPackage);
        baseEvent.setAppVersion(appVersion);
        baseEvent.setAppInstallStore(appStore);
        baseEvent.setHwBrand(hwBrand);
        baseEvent.setHwVersion(hwVersion);
        baseEvent.setHwTimezone(hwTimezone);
        baseEvent.setHwCountry(hwCountry);
        baseEvent.setHwLanguage(hwLanguage);
        baseEvent.setHwCarrier(hwCarrier);
        baseEvent.setHwScreenRes(hwScreenSize);
    }

    void setCountry(@NonNull String countryCode) {
        country = countryCode;
    }

    void setEmail(@NonNull String email) {
        hashedEmail = Utils.isEmptyString(email) ? email : new Hasher().SHA1Encode(email);
    }

    private void setAppParameters(Context context, AppInfo appInfo) {
        appName = appInfo.getAppName();
        appPackage = appInfo.getAppPackage();
        appVersion = appInfo.getAppVersion(context);
        appStore = appInfo.getAppStore(context);
    }

    private void setSdkParameters(SdkInfo sdkInfo) {
        apiKey = sdkInfo.getApiKey();
        sdkVersion = sdkInfo.getSdkVersion();
        sdkOs = sdkInfo.getSdkOs();
        eventHash = generateUUID();
        cookie = sdkInfo.getCookie();
    }

    private void setDeviceParameters(final Context context, DeviceInfo deviceInfo) {
        osBrand = deviceInfo.getOsBrand();
        osVersion = deviceInfo.getOsVersion();
        hwBrand = deviceInfo.getHwBrand();
        hwVersion = deviceInfo.getVersion();
        hwTimezone = deviceInfo.getTimeZone();
        hwCountry = deviceInfo.getCountry(context);
        hwLanguage = deviceInfo.getLanguage();
        hwCarrier = deviceInfo.getCarrier(context);
        hwScreenSize = deviceInfo.getScreen(context);
        deviceInfo.getAdvertiseId(context, new DeviceInfo.AdvertiseIdCallback() {
            @Override
            public void onFinish(String gaid) {
                mobileId = gaid;
            }
        });
    }

    @NonNull
    private String getPostId() {
        return eventHash + String.format(Locale.US, TRAILING_ZEROS, eventCounter);
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
