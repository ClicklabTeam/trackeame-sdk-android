package com.clicklab.sdk.repository;

import org.junit.Test;

import okhttp3.OkHttpClient;

import static org.junit.Assert.*;

public class OkHttpClientFactoryTest {
    @Test
    public void whenGetClientNonNullClientGiven() throws Exception {
        assertNotNull(new OkHttpClientFactory().getClient());
    }

    @Test
    public void whenGetClientThenCorrectTimeoutsHadBeenSet() throws Exception {
        OkHttpClient client = new OkHttpClientFactory().getClient();
        assertEquals(client.connectTimeoutMillis(), 10000);
        assertEquals(client.readTimeoutMillis(), 10000);
        assertEquals(client.writeTimeoutMillis(), 10000);
    }

}