package com.clicklab.sdk.utils;

import com.clicklab.sdk.Configuration;
import com.clicklab.sdk.Constants;
import com.clicklab.sdk.repository.SdkRepository;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SdkInfoTest {
    private static final String COOKIE = "COOKIE";
    private static final String APIKEY = "APIKEY";
    private static final String APPNAME = "APPNAME";
    private SdkInfo sdkInfo;
    private SdkRepository sdkRepository;
    private Configuration configuration;

    @Before
    public void setup() {
        sdkRepository = mock(SdkRepository.class);
        configuration = new Configuration(APIKEY, APPNAME);
        sdkInfo = new SdkInfo(sdkRepository, configuration);
    }

    @Test
    public void whenGetAPIKeyThenReturnCorrectAPIKey() {
        Assert.assertEquals(sdkInfo.getApiKey(), configuration.getApiKey());
    }

    @Test
    public void whenGetSdkVersionThenReturnCorrectVersion() {
        Assert.assertEquals(sdkInfo.getSdkVersion(), Constants.VERSION);
    }

    @Test
    public void whenGetOsBrandThenReturnAndroid() {
        Assert.assertEquals(sdkInfo.getSdkOs(), Constants.ANDROID);
    }

    @Test
    public void whenGetCookieThenReturnCorrectCookie() {
        when(sdkRepository.getCookie()).thenReturn(COOKIE);
        Assert.assertEquals(sdkInfo.getCookie(), COOKIE);
    }
}