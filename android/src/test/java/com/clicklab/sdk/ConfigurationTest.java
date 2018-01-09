package com.clicklab.sdk;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class ConfigurationTest {

    private static final String APIKEY = "APIKEY";
    private static final String APPNAME = "APPNAME";
    private static final String EMPTY_STRING = "";
    private static final String VALID_MAIL = "EMAIL@EMAIL.COM";
    private static final String INVALID_MAIL = "EMAIL@EMAIL.COM.";

    private Configuration configuration;

    @Before
    public void setup() {
        configuration = new Configuration(APIKEY, APPNAME);
    }

    @Test
    public void whenConfigurationIsGivenThenAPIKeyIsSet() {
        assertEquals(configuration.getApiKey(), APIKEY);
    }

    @Test
    public void whenConfigurationIsGivenThenAppnameIsSet() {
        assertEquals(configuration.getAppName(), APPNAME);
    }

    @Test
    public void whenNoEmailIsSetThenEmptyStringIsGiven() {
        assertEquals(configuration.getEmail(), EMPTY_STRING);
    }

    @Test
    public void whenNoCountryIsSetThenEmptyStringIsGiven() {
        assertEquals(configuration.getCountry(), EMPTY_STRING);
    }

    @Test
    public void whenEmailIsSetThenCorrectEmailIsGiven() {
        configuration.setEmail(VALID_MAIL);
        assertEquals(configuration.getEmail(), VALID_MAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenInvalidEmailIsSetThenExceptionIsThrown() {
        configuration.setEmail(INVALID_MAIL);
    }

    @Test
    public void whenCountryIsSetThenISOCountryIsGiven() {
        configuration.setLocaleCountry(Locale.ITALY);
        assertEquals(configuration.getCountry(), Locale.ITALY.getCountry());
    }

}