package com.clicklab.sdk.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.clicklab.sdk.Constants;
import com.clicklab.sdk.utils.device.AdvertiseId;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DeviceInfo {

    private static final String TIMES = "x";

    public interface AdvertiseIdCallback {
        void onFinish(String gaid);
    }

    /**
     * Gets the android version that is installed in the phone
     *
     * @return android version installed
     */
    public String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * Gets OS brand
     *
     * @return name of the OS
     */
    public String getOsBrand() {
        return Constants.ANDROID;
    }

    /**
     * Gets the device brand
     *
     * @return String
     */
    public String getHwBrand() {
        return Build.BRAND;
    }

    /**
     * Gets the device Model
     *
     * @return
     */
    public String getVersion() {
        return Build.MODEL;
    }

    /**
     * Gets the device TimeZone
     *
     * @return String
     */
    public String getTimeZone() {
        SimpleDateFormat formatter = new SimpleDateFormat("z", Locale.US);
        return formatter.format(new Date());
    }

    /**
     * Gets the Device Country
     *
     * @param context
     * @return
     */
    public String getCountry(Context context) {
        String country = getCountryFromNetwork(context);
        return !Utils.isEmptyString(country) ? country : getCountryFromLocale();
    }

    /**
     * Gets Device Language
     *
     * @return
     */
    public String getLanguage() {
        return Locale.getDefault().getLanguage().toUpperCase();
    }

    /**
     * Gets Device Carrier
     *
     * @return
     */
    public String getCarrier(Context context) {
        TelephonyManager manager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager != null ? manager.getNetworkOperatorName() : "";
    }

    /**
     * Gets device screen WithxHeight
     *
     * @return
     */
    public String getScreen(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels + TIMES + metrics.heightPixels;
    }

    /**
     * Gets Google Play Advertise ID
     *
     * @param context             Current context
     * @param advertiseIdCallback AdvertiseIdCallback defines what to do when the AdvertiseId is available
     */
    public void getAdvertiseId(final Context context, final AdvertiseIdCallback advertiseIdCallback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return new AdvertiseId().get(context);
            }

            @Override
            protected void onPostExecute(String advertId) {
                advertiseIdCallback.onFinish(advertId);
            }
        }.execute();
    }

    private String getCountryFromNetwork(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null && manager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
            String country = manager.getNetworkCountryIso();
            if (!Utils.isEmptyString(country)) {
                return country.toUpperCase(Locale.US);
            }
        }
        return "";
    }

    private String getCountryFromLocale() {
        return Locale.getDefault().getCountry();
    }
}
