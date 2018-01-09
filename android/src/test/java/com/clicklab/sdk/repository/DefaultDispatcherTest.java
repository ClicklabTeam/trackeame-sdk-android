package com.clicklab.sdk.repository;

import android.support.annotation.NonNull;

import com.clicklab.sdk.model.NetworkErrorSendException;
import com.clicklab.sdk.model.event.EventType;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;

import static com.clicklab.sdk.repository.DefaultDispatcherTest.ResponseType.OK_ERROR;
import static com.clicklab.sdk.repository.DefaultDispatcherTest.ResponseType.FAIL;
import static com.clicklab.sdk.repository.DefaultDispatcherTest.ResponseType.OK;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricTestRunner.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest({Response.class, RealResponseBody.class})
public class DefaultDispatcherTest {

    private static final String EMPTY_RESPONSE = "{}";

    enum ResponseType {
        OK, FAIL, OK_ERROR, NULL;
    }

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    DefaultDispatcher dispatcher;
    OkHttpClient client;
    Call call;
    Dispatcher.Callback callback;

    @Before
    public void setUp() {
        client = mock(OkHttpClient.class);
        callback = mock(Dispatcher.Callback.class);
        dispatcher = new DefaultDispatcher(client);
        dispatcher.setCallback(callback);
        call = mock(Call.class);
        when(client.newCall(any(Request.class))).thenReturn(call);

    }

    @Test(expected = NetworkErrorSendException.class)
    public void givenConnectionProblemsThenNetworkError() throws Exception {
        givenConnectionProblems();
        dispatcher.sendOneEvent(EMPTY_RESPONSE, EventType.generic);
        verifyCookieRecievedNoInteractions();
    }

    @Test
    public void givenResponseFailThenNothingExpected() throws Exception {
        generateResponse(FAIL, null);
        dispatcher.sendOneEvent(EMPTY_RESPONSE, EventType.generic);
        verifyCookieRecievedNoInteractions();
    }

    @Test
    public void givenOkResponseWhenNullBodyThenNothingExpected() throws Exception {
        generateResponse(OK, null);
        dispatcher.sendOneEvent(EMPTY_RESPONSE, EventType.generic);
        verifyCookieRecievedNoInteractions();
    }

    @Test
    public void givenOkResponseWhenEmptyBodyThenNothingExpected() throws Exception {
        generateResponse(OK, "");
        dispatcher.sendOneEvent(EMPTY_RESPONSE, EventType.generic);
        verifyCookieRecievedNoInteractions();
    }

    @Test
    public void givenOkResponseWhenNoCoockieBodyThenNothingExpected() throws Exception {
        generateResponse(OK, "{}");
        dispatcher.sendOneEvent(EMPTY_RESPONSE, EventType.generic);
        verifyCookieRecievedNoInteractions();
    }

    @Test
    public void givenOkResponseWhenMalformedJsonBodyThenNothingExpected() throws Exception {
        generateResponse(OK, "{");
        dispatcher.sendOneEvent(EMPTY_RESPONSE, EventType.generic);
        verifyCookieRecievedNoInteractions();
    }

    @Test
    public void givenOkResponseWhenIOExceptionThenNothingExpected() throws Exception {
        generateResponse(OK_ERROR, "");
        dispatcher.sendOneEvent(EMPTY_RESPONSE, EventType.generic);
        verifyCookieRecievedNoInteractions();
    }

    @Test
    public void givenOkResponseWhenCoockieBodyThenCallbackExpected() throws Exception {
        generateResponse(OK, "{'trackeame_cookie':'123456'}");
        dispatcher.sendOneEvent(EMPTY_RESPONSE, EventType.generic);
        verifyCookieRecievedOneInteraction("123456");
    }

    private void givenConnectionProblems() {
        try {
            when(call.execute()).thenThrow(new IOException());
        } catch (IOException e) {
            fail(e.toString());
        }
    }

    private void generateResponse(ResponseType responseType, String responseString) {
        try {
            Response response = getResponse(responseType);
            ResponseBody responseBody;
            switch (responseType) {
                case OK:
                    responseBody = PowerMockito.mock(RealResponseBody.class);
                    when(responseBody.string()).thenReturn(responseString);
                    when(response.body()).thenReturn(responseBody);
                    break;
                case NULL:
                    when(response.body()).thenReturn(null);
                    break;
                case OK_ERROR:
                    responseBody = PowerMockito.mock(RealResponseBody.class);
                    when(responseBody.string()).thenThrow(IOException.class);
                    when(response.body()).thenReturn(responseBody);
                    break;
            }

            when(call.execute()).thenReturn(response);
        } catch (IOException e) {
            fail(e.toString());
        }
    }

    @NonNull
    private Response getResponse(ResponseType responseType) {
        Response response = PowerMockito.mock(Response.class);
        when(response.isSuccessful()).thenReturn(getResponseSuccessfulness(responseType));
        return response;
    }

    private boolean getResponseSuccessfulness(ResponseType responseType) {
        return OK.equals(responseType) || OK_ERROR.equals(responseType);
    }

    private void verifyCookieRecievedNoInteractions() {
        verify(callback, times(0)).cookieRecieved(anyString());
    }

    private void verifyCookieRecievedOneInteraction(String cookie) {
        verify(callback, times(1)).cookieRecieved(cookie);
    }
}