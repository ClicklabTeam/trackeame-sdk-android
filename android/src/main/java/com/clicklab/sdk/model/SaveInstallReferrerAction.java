package com.clicklab.sdk.model;


import android.content.Context;

import com.clicklab.sdk.repository.SdkRepositoryFactory;

public class SaveInstallReferrerAction {
    public void execute(Context context, String referrer) {
        new SdkRepositoryFactory().getSdkRepository(context).setInstallReferrer(referrer);
    }
}
