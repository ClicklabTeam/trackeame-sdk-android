package com.clicklab.sdk;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.clicklab.sdk.model.SaveInstallReferrerAction;
import com.clicklab.sdk.utils.Utils;

public class ReferrerCatcher extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getExtras() != null ? intent.getExtras().getString("referrer") : "";
        if (!Utils.isEmptyString(referrer)) {
            new SaveInstallReferrerAction().execute(context, referrer);
        }
    }
}