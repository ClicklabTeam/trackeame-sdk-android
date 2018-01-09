package com.clicklab.sdk.utils;

import com.clicklab.sdk.Configuration;
import com.clicklab.sdk.Constants;
import com.clicklab.sdk.repository.SdkRepository;

public class SdkInfo {

    private SdkRepository sdkRepository;
    private Configuration configuration;

    public SdkInfo(SdkRepository sdkRepository, Configuration configuration) {
        this.sdkRepository = sdkRepository;
        this.configuration = configuration;
    }

    /**
     * Gets the ApiKey from Configuration
     *
     * @return String apiKey
     */
    public String getApiKey() {
        return configuration.getApiKey();
    }

    /**
     * Gets the SDK versionName
     *
     * @return
     */
    public String getSdkVersion() {
        return Constants.VERSION;
    }

    /**
     * Gets the OS that the SDK is installed
     *
     * @return
     */
    public String getSdkOs() {
        return Constants.ANDROID;
    }

    /**
     * Gets the current cookie stored cookie given by the API
     *
     * @return String containing the stored cookie
     */
    public String getCookie() {
        return sdkRepository.getCookie();
    }
}
