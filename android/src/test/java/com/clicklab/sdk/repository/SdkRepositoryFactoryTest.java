package com.clicklab.sdk.repository;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(RobolectricTestRunner.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@Config(manifest = Config.NONE)
public class SdkRepositoryFactoryTest {
    @Test
    public void whenGetSdkRepositoryThenNonNullReposositoryIsGiven() throws Exception {
        assertNotNull(new SdkRepositoryFactory().getSdkRepository(mock(Context.class)));
    }
}