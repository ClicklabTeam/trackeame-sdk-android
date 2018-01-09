package com.clicklab.sdk.repository;

public interface SdkRepository {
    String getCookie();

    void setCookie(String Cookie);

    void setInstallReferrer(String referrer);

    String getInstallReferrer();

    void setAppInstallTracked();

    boolean getAppInstallTracked();
}
