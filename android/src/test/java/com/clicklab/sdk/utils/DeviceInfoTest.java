package com.clicklab.sdk.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import com.clicklab.sdk.Constants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowTelephonyManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricTestRunner.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest({Build.class, Build.VERSION.class})
@Config(manifest = Config.NONE)
public class DeviceInfoTest {

    private static final Locale TEST_LOCALE_1 = Locale.ITALY;
    private static final String TEST_COUNTRY_1 = TEST_LOCALE_1.getCountry();
    private static final Locale TEST_LOCALE_2 = Locale.CANADA;
    private static final String TEST_COUNTRY_2 = TEST_LOCALE_2.getCountry();
    private static final String TEST_OPERATOR = "TEST_OPERATOR";

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private DeviceInfo deviceInfo;

    @Before
    public void setUp() throws Exception {
        deviceInfo = new DeviceInfo();
        PowerMockito.mockStatic(Build.class);
    }

    @Test
    public void whenGetOsVersionThenReturnCorrectValue() throws Exception {
        Whitebox.setInternalState(Build.VERSION.class, "RELEASE", "nougat");
        assertEquals(deviceInfo.getOsVersion(), Build.VERSION.RELEASE);
    }

    @Test
    public void whenGetgetOsBrandThenReturnCorrectValue() throws Exception {
        assertEquals(deviceInfo.getOsBrand(), Constants.ANDROID);
    }

    @Test
    public void getHwBrandThenReturnCorrectValue() throws Exception {
        Whitebox.setInternalState(Build.class, "BRAND", "phoneBrand");
        assertEquals(deviceInfo.getHwBrand(), Build.BRAND);
    }

    @Test
    public void getVersionThenReturnCorrectValue() throws Exception {
        Whitebox.setInternalState(Build.class, "MODEL", "model");
        assertEquals(deviceInfo.getVersion(), Build.MODEL);
    }

    @Test
    public void getTimeZoneThenReturnCorrectValue() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("z", Locale.US);
        assertEquals(deviceInfo.getTimeZone(), formatter.format(new Date()));
    }

    @Test
    public void givenNetworkCountryIsSetWhenPhoneIsGSMThenReturnNetorkCountry() throws Exception {
        Context context = ShadowApplication.getInstance().getApplicationContext();
        ShadowTelephonyManager manager = getTelephonyManager(context);
        givenNetworkCountryIsSet(manager);
        manager.setPhoneType(TelephonyManager.PHONE_TYPE_GSM);

        DeviceInfo deviceInfo = new DeviceInfo();
        assertEquals(TEST_COUNTRY_1, deviceInfo.getCountry(context));
    }

    @Test
    public void givenTestCountryWhenPhoneIsCDMAThenLocaleCountry() {
        Context context = ShadowApplication.getInstance().getApplicationContext();
        ShadowTelephonyManager manager = getTelephonyManager(context);
        givenNetworkCountryIsSet(manager);
        manager.setPhoneType(TelephonyManager.PHONE_TYPE_CDMA);

        Locale.setDefault(TEST_LOCALE_2);

        DeviceInfo deviceInfo = new DeviceInfo();
        assertEquals(TEST_COUNTRY_2, deviceInfo.getCountry(context));
    }


    @Test
    public void whenNoNetworkCountryThenReturnLocaleCountry() throws Exception {
        Context context = ShadowApplication.getInstance().getApplicationContext();
        Locale.setDefault(TEST_LOCALE_1);
        assertEquals(TEST_COUNTRY_1, deviceInfo.getCountry(context));
    }


    @Test
    public void getLanguageThenReturnCorrectValue() throws Exception {
        Locale.setDefault(TEST_LOCALE_1);
        assertEquals(TEST_LOCALE_1.getLanguage().toUpperCase(), deviceInfo.getLanguage());
    }

    @Test
    public void whenIsACarrierThenReturnCarrier() throws Exception {
        TelephonyManager manager = configureTelefonyManagerToReturnOperatorName(TEST_OPERATOR);
        Context context = configureTelefonyServiceToReturnManager(manager);

        assertEquals(TEST_OPERATOR, deviceInfo.getCarrier(context));
    }

    @Test
    public void whenNoCarrierThenReturnEmptyCarrier() throws Exception {
        TelephonyManager manager = configureTelefonyManagerToReturnOperatorName("");
        Context context = configureTelefonyServiceToReturnManager(manager);

        assertEquals("", deviceInfo.getCarrier(context));
    }

    @Test
    public void whenNoTelefonyServiceThenReturnEmptyCarrier() throws Exception {
        Context context = configureTelefonyServiceToReturnManager(null);
        assertEquals("", deviceInfo.getCarrier(context));
    }

    @NonNull
    private TelephonyManager configureTelefonyManagerToReturnOperatorName(String value) {
        TelephonyManager manager = mock(TelephonyManager.class);
        when(manager.getNetworkOperatorName()).thenReturn(value);
        return manager;
    }

    @NonNull
    private Context configureTelefonyServiceToReturnManager(TelephonyManager manager) {
        Context context = mock(Context.class);
        when(context.getSystemService(Context.TELEPHONY_SERVICE)).thenReturn(manager);
        return context;
    }

    private void givenNetworkCountryIsSet(ShadowTelephonyManager manager) {
        manager.setNetworkCountryIso(TEST_COUNTRY_1);
    }

    private ShadowTelephonyManager getTelephonyManager(Context context) {
        return Shadows.shadowOf((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE));
    }
}