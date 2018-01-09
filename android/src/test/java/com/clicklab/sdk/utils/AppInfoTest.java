package com.clicklab.sdk.utils;

import android.app.Application;

import com.clicklab.sdk.Configuration;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppInfoTest {

    private static final String PACKAGE_NAME = "package_name";
    private static final String APIKEY = "APIKEY";
    private static final String APPNAME = "APPNAME";
    private Application app;
    private AppInfo appInfo;
    private Configuration configuration;

    @Before
    public void setUp(){
        app = mock(Application.class);
        when(app.getPackageName()).thenReturn(PACKAGE_NAME);
        configuration = new Configuration(APIKEY, APPNAME);
        appInfo = new AppInfo(app, configuration);
    }

    @Test
    public void whenGetAppPackageThenCorrectPackageGiven(){
        assertEquals(appInfo.getAppPackage(), PACKAGE_NAME);
    }

    @Test
    public void whenGetAppNameThenCorrectNameGiven(){
        assertEquals(appInfo.getAppName(), APPNAME);
    }
}