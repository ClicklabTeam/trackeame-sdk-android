package com.clicklab.sdk.utils.device;


import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.clicklab.sdk.Constants;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

public class AdvertiseId {
    private static final String AMAZON = "Amazon";

    private static final String SETTING_ADVERTISING_ID = "advertising_id";

    /**
     * Gets the current advertise id given by the phone
     *
     * @param context current context
     * @return GAID - Amazon Advertise Id - or an empty string in case it couldn't be resolved
     */
    public String get(final Context context) {
        if (AMAZON.equalsIgnoreCase(Build.MANUFACTURER)) {
            return getAmazonAdvertiseId(context);
        } else {
            return getGoogleAdvertiseId(context);
        }
    }

    private String getAmazonAdvertiseId(Context context) {
        ContentResolver cr = context.getContentResolver();
        String advertiseId = Settings.Secure.getString(cr, SETTING_ADVERTISING_ID);
        return advertiseId == null ? "" : advertiseId;
    }

    private String getGoogleAdvertiseId(final Context context) {
        AdvertisingIdClient.Info idInfo = null;
        try {
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS) {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context.getApplicationContext());
            }
        } catch (IOException | GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Log.d(Constants.TAG, "Unnable to get GAID");
        }
        return idInfo != null ? idInfo.getId() : "";
    }
}
