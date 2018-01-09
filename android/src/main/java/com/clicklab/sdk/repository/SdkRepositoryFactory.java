package com.clicklab.sdk.repository;

import android.content.Context;

public class SdkRepositoryFactory {
    DefaultSdkRepository defaultSdkRepository;

    public SdkRepository getSdkRepository(Context context) {
        if (defaultSdkRepository == null) {
            defaultSdkRepository = new DefaultSdkRepository(context);
        }
        return defaultSdkRepository;
    }
}
