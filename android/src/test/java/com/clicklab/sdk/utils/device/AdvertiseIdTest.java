package com.clicklab.sdk.utils.device;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.util.ReflectionHelpers;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricTestRunner.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest({AdvertisingIdClient.class, GoogleApiAvailability.class})
@Config(manifest = Config.NONE)
public class AdvertiseIdTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private static final String TEST_ADVERTISING_ID = "advertisingId";
    private static final String AMAZON_MANUFACTURER = "Amazon";
    private static final String OTHER_MANUFACTURER = "Other";


    Context context;

    @Before
    public void setUp() {
        context = ShadowApplication.getInstance().getApplicationContext();
    }

    @Test
    public void givenAmazonDeviceThenGetAmazonAdvertiseId() {
        givenAmazonManufacturer();

        assertEquals(TEST_ADVERTISING_ID, new AdvertiseId().get(context));
    }

    @Test
    public void givenAnyOtherManufacturerWhenGooglePlayConectsThenGetGAID() throws Exception {
        GoogleApiAvailability googleApiAvailability = givenGoogleManufacturer();

        whenAdvertisingIdInfoIsReturned();
        whenConnectionResultIs(googleApiAvailability, ConnectionResult.SUCCESS);

        assertEquals(TEST_ADVERTISING_ID, new AdvertiseId().get(context));
    }


    @Test
    public void givenAnyOtherManufacturerWhenGooglePlayNotConectsThenGetEmptyString() throws Exception {
        GoogleApiAvailability googleApiAvailability = givenGoogleManufacturer();

        whenAdvertisingIdInfoIsReturned();
        whenConnectionResultIs(googleApiAvailability, ConnectionResult.INTERNAL_ERROR);

        assertEquals("", new AdvertiseId().get(context));
    }

    @Test
    public void givenAnyOtherManufacturerWhenExceptionThenGetEmptyString() throws Exception {
        GoogleApiAvailability googleApiAvailability = givenGoogleManufacturer();

        whenConnectionResultIs(googleApiAvailability, ConnectionResult.SUCCESS);
        whenConnectionThrowsException(googleApiAvailability);

        assertEquals("", new AdvertiseId().get(context));
    }

    private void whenConnectionResultIs(GoogleApiAvailability googleApiAvailability, int result) {
        when(googleApiAvailability.isGooglePlayServicesAvailable(context)).thenReturn(result);
    }

    private void whenConnectionThrowsException(GoogleApiAvailability googleApiAvailability) throws GooglePlayServicesNotAvailableException, IOException, GooglePlayServicesRepairableException {
        PowerMockito.mockStatic(AdvertisingIdClient.class);
        when(AdvertisingIdClient.getAdvertisingIdInfo(context)).thenThrow(new GooglePlayServicesNotAvailableException(0));


    }

    private GoogleApiAvailability givenGoogleManufacturer() {
        setManufacturer(OTHER_MANUFACTURER);
        PowerMockito.mockStatic(AdvertisingIdClient.class);

        PowerMockito.mockStatic(GoogleApiAvailability.class);
        GoogleApiAvailability googleApiAvailability = mock(GoogleApiAvailability.class);
        when(GoogleApiAvailability.getInstance()).thenReturn(googleApiAvailability);
        return googleApiAvailability;
    }

    private void whenAdvertisingIdInfoIsReturned() {
        AdvertisingIdClient.Info info = new AdvertisingIdClient.Info(
                TEST_ADVERTISING_ID,
                false
        );
        try {
            when(AdvertisingIdClient.getAdvertisingIdInfo(context)).thenReturn(info);
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    private void givenAmazonManufacturer() {
        setManufacturer(AMAZON_MANUFACTURER);
        ContentResolver cr = context.getContentResolver();
        Settings.Secure.putString(cr, "advertising_id", TEST_ADVERTISING_ID);
    }

    private void setManufacturer(String manufacturer) {
        ReflectionHelpers.setStaticField(Build.class, "MANUFACTURER", manufacturer);
    }
}